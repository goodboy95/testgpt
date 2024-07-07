package com.seekerhut.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seekerhut.utils.EnumAndConstData.CharRangeKey;
import org.apache.http.HttpException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class CommonFunctions {
    private static CloseableHttpClient client;
    private static Logger logger;
    private static DateTimeFormatter dateFormatter;
    static {
        client = HttpClients.createDefault();
        logger = LoggerFactory.getLogger(CommonFunctions.class);
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    private static HashMap<String, AtomicLong> currentPrimaryKeyId = new HashMap<>();
    private static HashMap<String, Long> currentPrimaryKeyMaxId = new HashMap<>();
    private static HashMap<String, String> prevUsingKey = new HashMap<>();

    /**
     * 主键ID发号器，生成趋势递增ID，保证同一秒内生成的ID可以排到一起（目前使用Redis防止意外重启后ID回退，可以改造为记录到文件）
     * @param tableName 表名
     * @return 主键ID
     */
    public static long generatePrimaryKeyId(String tableName) {
        // 每次从redis申请1000个id空间在本机使用，减少和redis的交互次数
        var idsPerRequest = 1000;
        var baseTime = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        var curTime = LocalDateTime.now();
        // 当前时间距离baseTime相隔的秒数，32bit（136.2年）
        var minutesToBaseTime = curTime.toEpochSecond(ZoneOffset.UTC) - baseTime.toEpochSecond(ZoneOffset.UTC);
        // redis节点编号，9bit（512个）
        var nodeNum = 0;
        // 余下的22bit用来生成id，大约每节点每秒四百万个(TODO: 如果某一秒把ID用完了，可以返回-1让用户重新取号)
        long idBase = (minutesToBaseTime << 37) + (nodeNum << 28);
        var tblKeyBase = ConstValues.RedisKeys.PrimaryKeyIdPrefix + tableName;
        var tblKey = String.format("%s_%d_%d", tblKeyBase, minutesToBaseTime, nodeNum);
        if (!currentPrimaryKeyId.containsKey(tblKey)) {
            currentPrimaryKeyId.put(tblKey, new AtomicLong(0l));
        }
        long maxId = currentPrimaryKeyMaxId.getOrDefault(tblKey, 0l);
        long finalPKId = currentPrimaryKeyId.getOrDefault(tblKey, new AtomicLong(0l)).incrementAndGet();
        if (finalPKId >= maxId) {
            synchronized (currentPrimaryKeyId) {
                maxId = currentPrimaryKeyMaxId.getOrDefault(tblKey, 0l);
                if (finalPKId >= maxId) {
                    maxId = JedisHelper.incrBy(tblKey, idsPerRequest);
                    JedisHelper.expire(tblKey, 60 * 2);
                    currentPrimaryKeyId.get(tblKey).set(maxId - idsPerRequest);
                    currentPrimaryKeyMaxId.put(tblKey, maxId);
                }
                finalPKId = currentPrimaryKeyId.get(tblKey).incrementAndGet();
                // 清理旧键值
                if (prevUsingKey.containsKey(tblKeyBase) && !prevUsingKey.get(tblKeyBase).equals(tblKey)) {
                    var oldKey = prevUsingKey.get(tblKeyBase);
                    currentPrimaryKeyId.remove(oldKey);
                    currentPrimaryKeyMaxId.remove(oldKey);
                }
                prevUsingKey.put(tblKeyBase, tblKey);
            }
        }
        return idBase + finalPKId;
    }

    public static String postRequest(String url, JSONObject body, HashMap<String, String> headers) throws Exception {
        var postWrapper = new HttpPost(url);
        if (headers != null) {
            for (String k : headers.keySet()) {
                postWrapper.setHeader(k, headers.get(k));
            }
        }
        System.out.println(body.toJSONString());
        postWrapper.setEntity(new StringEntity(body.toJSONString(), Charset.forName("UTF-8")));
        var resultResp = client.execute(postWrapper);
        int statusCode = resultResp.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            throw new HttpException(String.format("Failed to call api %s, errCode: %s", url, statusCode));
        }
        var result = EntityUtils.toString(resultResp.getEntity());
        return result;
    }

    /**
     * 执行sha256运算
     * @param input
     * @return
     */
    public static String getSHA256Hash(String input) {
        try {
            // 创建SHA-256消息摘要对象
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // 更新要哈希的数据
            md.update(input.getBytes(StandardCharsets.UTF_8));
            // 完成哈希计算，得到结果
            byte[] digest = md.digest();
            // 将结果转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256算法不可用", e);
        }
    }

    /**
     * GLM公共请求方法
     * @param apikey apikey
     * @param modelName 模型名称
     * @param prompt 提示语
     * @param historyData 历史数据
     * @return GLM模型返回的提示语
     * @throws Exception
     */
    public static String GLMCommonRequest(String apikey, String userqq, String modelName, String prompt, JSONArray historyData) throws Exception {
        var url = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
        var chatBody = new JSONObject() {
            {
                put("role", "user");
                put("content", prompt);
            }
        };
        historyData.add(chatBody);
        var submitData = new JSONObject() {
            {
                put("model", modelName);
                put("messages", historyData);
                put("user_id", userqq);
            }
        };
        var headers = new HashMap<String, String>() {
            {
                put("Authorization", String.format("Bearer %s", apikey));
                put("Content-Type", "application/json");
            }
        };
        var gptRawResult = CommonFunctions.postRequest(url, submitData, headers);
        var gptJsonResult = JSONObject.parseObject(gptRawResult);
        var completionData = gptJsonResult.getJSONArray("choices").getJSONObject(0).getJSONObject("message");
        var tokenUsage = gptJsonResult.getJSONObject("usage");
        var promptTokens = tokenUsage.getInteger("prompt_tokens");
        var completionTokens = tokenUsage.getInteger("completion_tokens");
        logger.info(String.join(String.format("userqq: %s, promptToken: %s, completionToken: %s", userqq, promptTokens.toString(), completionTokens.toString())));
        JedisHelper.incrBy(String.format("usedTokens:%s:%s", userqq, LocalDate.now().format(dateFormatter)), promptTokens + completionTokens);
        var completionStr = completionData.getString("content");
        return completionStr;
    }

    /**
     * GLM知识库请求方法
     * @param apikey apikey
     * @param modelName 模型名称
     * @param prompt 提示语
     * @param kbid 知识库id
     * @return GLM模型返回的提示语
     * @throws Exception
     */
    public static String GLMKnowledgeBaseRequest(String apikey, String userqq, String modelName, String prompt, String kbid) throws Exception {
        var url = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
        var submitDialog = new ArrayList<JSONObject>();
        var chatBody = new JSONObject() {
            {
                put("role", "user");
                put("content", prompt);
            }
        };
        submitDialog.add(chatBody);
        var retrievalData = new JSONObject() {
            {
                put("knowledge_id", kbid);
            }
        };
        var toolData = new JSONObject() {
            {
                put("type", "retrieval");
                put("retrieval", retrievalData);
            }
        };
        var tools = new JSONArray();
        tools.add(toolData);
        var submitData = new JSONObject() {
            {
                put("model", modelName);
                put("messages", submitDialog);
                put("temperature", 0.1);
                put("tools", tools);
                put("user_id", userqq);
            }
        };
        var headers = new HashMap<String, String>() {
            {
                put("Authorization", String.format("Bearer %s", apikey));
                put("Content-Type", "application/json");
            }
        };
        var gptRawResult = CommonFunctions.postRequest(url, submitData, headers);
        var gptJsonResult = JSONObject.parseObject(gptRawResult);
        var completionData = gptJsonResult.getJSONArray("choices").getJSONObject(0).getJSONObject("message");
        var tokenUsage = gptJsonResult.getJSONObject("usage");
        var promptTokens = tokenUsage.getInteger("prompt_tokens");
        var completionTokens = tokenUsage.getInteger("completion_tokens");
        logger.info(String.join(String.format("userqq: %s, promptToken: %s, completionToken: %s", userqq, promptTokens.toString(), completionTokens.toString())));
        JedisHelper.incrBy(String.format("usedTokens:%s:%s", userqq, LocalDate.now().format(dateFormatter)), promptTokens + completionTokens);
        var completionStr = completionData.getString("content");
        return completionStr;
    }
}