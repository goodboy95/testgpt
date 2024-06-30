package com.seekerhut.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seekerhut.model.bean.CharacterBean;
import com.seekerhut.model.bean.DialogDataBean;
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
    public HashMap<Long, DialogDataBean> dialogMap = new HashMap<>();
    public ClientApiController() {
    }

    @PostMapping("set_character_system_prompt")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "replyJson", value = "", paramType = "body", dataType = "String"),
    })
    public @ResponseBody String getCharacterSystemPrompt(String userToken, String characterName, String characterPrompt) {
        try {
            String userName = "testUser";
            JedisHelper.set(String.format("%s:%s", userName, characterName), characterPrompt);
            return Success("ok");
        }
        catch (Exception e) {
            return Fail(-1, "msg: " + e.getMessage() + ", stack: " + String.join("\n", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.toList())));
        }
    }

    @PostMapping("get_character_system_prompt")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "replyJson", value = "", paramType = "body", dataType = "String"),
    })
    public @ResponseBody String getCharacterSystemPrompt(String userToken, String characterName) {
        try {
            String userName = "testUser";
            var systemPrompt = JedisHelper.get(String.format("%s:%s", userName, characterName), String.class);
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
    public @ResponseBody String chat(String userToken, String msg, String chatHistory) {
        try {
            var chatHistoryJArray = JSONArray.parseArray(chatHistory);
            String returnData = CommonFunctions.GLMCommonRequest("b1d8aee88ec4528a93a3ca834c57714a.e1wBdUKRnAFFwXJD", "glm-4-flash", msg, chatHistoryJArray);
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
    public @ResponseBody String kbRetrieve(String userToken, String kbid, String msg) {
        try {
            var resp = CommonFunctions.GLMKnowledgeBaseRequest("b1d8aee88ec4528a93a3ca834c57714a.e1wBdUKRnAFFwXJD", "glm-4-flash", msg, kbid);
            return Success(resp);
        }
        catch (Exception e) {
            return Fail(-1, "msg: " + e.getMessage() + ", stack: " + String.join("\n", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.toList())));
        }
    }


}
