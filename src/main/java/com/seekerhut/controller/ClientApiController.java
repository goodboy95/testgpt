package com.seekerhut.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seekerhut.model.config.BaseConfig;
import com.seekerhut.service.EmotionListService;
import com.seekerhut.utils.CommonFunctions;
import com.seekerhut.utils.ConstValues;
import com.seekerhut.utils.JedisHelper;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/client")
public class ClientApiController extends BaseController {
    @Resource
    private BaseConfig baseConfig;

    //public String gptApi = "https://api.chatanywhere.com.cn/v1/chat/completions";
    //public String gptToken = "";
    public String voiceApi = "";
    public String glmApi = "";
    public String voiceToken = "sk-5547696b5c5d1b88c5af4f12832dba37";
    public String glmToken = "b1d8aee88ec4528a93a3ca834c57714a.e1wBdUKRnAFFwXJD";
    private EmotionListService emotionListService;
    public ClientApiController(EmotionListService emotionListService) {
        this.emotionListService = emotionListService;
    }

    @CrossOrigin
    @PostMapping("set_character_system_prompt")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "replyJson", value = "", paramType = "body", dataType = "String"),
    })
    public @ResponseBody String setCharacterSystemPrompt(@CookieValue String userqq, String characterName, String characterPrompt) {
        try {
            JedisHelper.set(String.format("%s:prompt:%s", userqq, characterName), characterPrompt);
            return Success("ok");
        }
        catch (Exception e) {
            return Fail(-1, "msg: " + e.getMessage() + ", stack: " + String.join("\n", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.toList())));
        }
    }

    @CrossOrigin
    @GetMapping("get_character_system_prompt")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "replyJson", value = "", paramType = "body", dataType = "String"),
    })
    public @ResponseBody String getCharacterSystemPrompt(@CookieValue String userqq, String characterName) {
        try {
            var systemPrompt = JedisHelper.get(String.format("%s:prompt:%s", userqq, characterName));
            if (systemPrompt == null || systemPrompt.length() == 0) {
                systemPrompt = JedisHelper.get(String.format("%s:prompt:%s", "testUser", characterName));
                if (systemPrompt == null || systemPrompt.length() == 0) {
                    return Fail("Character not found.");
                }
            }
            return Success(systemPrompt);
        }
        catch (Exception e) {
            return Fail(-1, "msg: " + e.getMessage() + ", stack: " + String.join("\n", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.toList())));
        }
    }

    @CrossOrigin
    @PostMapping("set_chatlog")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "replyJson", value = "", paramType = "body", dataType = "String"),
    })
    public @ResponseBody String setChatlog(@CookieValue String userqq, String characterName, String chatlog) {
        try {
            JedisHelper.set(String.format("%s:chatlog:%s", userqq, characterName), chatlog);
            return Success("ok");
        }
        catch (Exception e) {
            return Fail(-1, "msg: " + e.getMessage() + ", stack: " + String.join("\n", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.toList())));
        }
    }

    @CrossOrigin
    @GetMapping("get_chatlog")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "replyJson", value = "", paramType = "body", dataType = "String"),
    })
    public @ResponseBody String getChatlog(@CookieValue String userqq, String characterName) {
        try {
            var systemPrompt = JedisHelper.get(String.format("%s:chatlog:%s", userqq, characterName));
            if (systemPrompt == null || systemPrompt.length() == 0) {
                return Fail("Character not found.");
            }
            return Success(systemPrompt);
        }
        catch (Exception e) {
            return Fail(-1, "msg: " + e.getMessage() + ", stack: " + String.join("\n", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.toList())));
        }
    }

    @CrossOrigin
    @PostMapping("chat")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "replyJson", value = "", paramType = "body", dataType = "String"),
    })
    public @ResponseBody String chat(@CookieValue String userqq, String msg, String chatHistory) {
        try {
            var chatHistoryJArray = JSONArray.parseArray(chatHistory);
            String returnData = CommonFunctions.GLMCommonRequest(glmToken, userqq, "glm-4-flash", msg, chatHistoryJArray);
            return Success(returnData);
        }
        catch (Exception e) {
            return Fail(-1, "msg: " + e.getMessage() + ", stack: " + String.join("\n", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.toList())));
        }
    }

    @CrossOrigin
    @PostMapping("kb_retrieve")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "replyJson", value = "", paramType = "body", dataType = "String"),
    })
    public @ResponseBody String kbRetrieve(@CookieValue String userqq, String kbid, String msg) {
        try {
            var resp = CommonFunctions.GLMKnowledgeBaseRequest(glmToken, userqq, "glm-4-flash", msg, kbid);
            return Success(resp);
        }
        catch (Exception e) {
            return Fail(-1, "msg: " + e.getMessage() + ", stack: " + String.join("\n", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.toList())));
        }
    }

    /**
     * 为AI的回复生成语音
     * @param userqq
     * @param characterId 角色id
     * @param answerText AI回复的文本（生成的就是该文本的语音）
     * @param askText 用户询问的文本（用于参考，辅助确认AI回复的情感）
     * @return
     */
    @CrossOrigin
    @PostMapping("voice_generate")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "replyJson", value = "", paramType = "body", dataType = "String"),
    })

    public @ResponseBody String voiceGenerate(@CookieValue String userqq, String characterId, String answerText, String askText) {
        try {
            // analyse emotion style from text
            askText = askText.replace("“", "‘").replace("”", "’");
            answerText = answerText.replace("“", "‘").replace("”", "’");
            var promptBase = "我需要判断A和B两人对话中，B可能表现出来的情感。目前支持的情感有以下几种：1、平静略带忧郁，2、平静略带欣喜，3、平静，4、轻松愉快，5、疑惑不解，6、愤怒，7、感叹，8、焦急，9、兴奋，0、情感未知或不属于之前9种。在回复时，仅应当回复一个0-6的数字，不要回复其他任何东西。以下是A和B的对话。\nA：“%s”，B：“%s”";
            var emotionJudgePrompt = String.format(promptBase, askText, answerText);
            String emotionNum = CommonFunctions.GLMCommonRequest(glmToken, userqq, "glm-4-flash", emotionJudgePrompt, new JSONArray());
            var styleString = CommonFunctions.GetVoiceStyleIdByEmotionNum(emotionNum);
            // get voice response
            var resp = CommonFunctions.VoiceGenerate(voiceToken, userqq, answerText, characterId, styleString);
            return Success(resp);
        }
        catch (Exception e) {
            return Fail(-1, "msg: " + e.getMessage() + ", stack: " + String.join("\n", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.toList())));
        }
    }

    @CrossOrigin
    @PostMapping("emotion_list")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "replyJson", value = "", paramType = "body", dataType = "String"),
    })
    public @ResponseBody String getEmotionList(@CookieValue String userqq) {
        try {
            var emotionList = emotionListService.getEmotionPicList();
            var returnData = new JSONArray();
            for (var emotionPic : emotionList) {
                returnData.add((JSONObject)JSONObject.toJSON(emotionPic));
            }
            return Success(returnData);
        }
        catch (Exception e) {
            return Fail(-1, "msg: " + e.getMessage() + ", stack: " + String.join("\n", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.toList())));
        }
    }
}
