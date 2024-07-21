package com.seekerhut.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.seekerhut.ChatRequest;
import com.seekerhut.ChatResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")  // 允许跨域访问
public class ChatController {

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest chatRequest) {
        String apiUrl = chatRequest.getUrl();
        String apiKey = chatRequest.getApiKey();
        String model = chatRequest.getModel();
        List<ChatRequest.Message> messages = chatRequest.getMessages();

        // 创建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", messages);

        // 将请求体转换为 JSON 字符串
        String jsonRequest = JSON.toJSONString(requestBody);

        // 使用 Apache HttpClient 发送 POST 请求
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(apiUrl);
            httpPost.setHeader("Authorization", "Bearer " + apiKey);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonRequest, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                if (statusCode == 200) {
                    // 解析响应 JSON
                    JSONObject jsonResponse = JSON.parseObject(responseBody);
                    JSONArray choices = jsonResponse.getJSONArray("choices");

                    if (choices != null && !choices.isEmpty()) {
                        JSONObject firstChoice = choices.getJSONObject(0);
                        JSONObject message = firstChoice.getJSONObject("message");

                        if (message != null) {
                            String content = message.getString("content");
                            return ResponseEntity.ok(new ChatResponse(content));
                        }
                    }
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ChatResponse("Error: No response content"));
                } else {
                    return ResponseEntity.status(statusCode)
                            .body(new ChatResponse("Error: " + responseBody));
                }
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ChatResponse("Error: " + e.getMessage()));
        }
    }
}