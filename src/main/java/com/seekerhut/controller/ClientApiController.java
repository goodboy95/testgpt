package com.seekerhut.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seekerhut.model.config.BaseConfig;
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

    public String gptApi = "https://api.chatanywhere.com.cn/v1/chat/completions";
    public String voiceApi = "";
    public ClientApiController() {
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
            String returnData = CommonFunctions.GLMCommonRequest("b1d8aee88ec4528a93a3ca834c57714a.e1wBdUKRnAFFwXJD", userqq, "glm-4-flash", msg, chatHistoryJArray);
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
            var resp = CommonFunctions.GLMKnowledgeBaseRequest("b1d8aee88ec4528a93a3ca834c57714a.e1wBdUKRnAFFwXJD", userqq, "glm-4-flash", msg, kbid);
            return Success(resp);
        }
        catch (Exception e) {
            return Fail(-1, "msg: " + e.getMessage() + ", stack: " + String.join("\n", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.toList())));
        }
    }
}
