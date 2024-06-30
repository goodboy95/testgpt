package com.seekerhut.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seekerhut.model.bean.CharacterBean;
import com.seekerhut.model.bean.DialogDataBean;
import com.seekerhut.model.config.BaseConfig;
import com.seekerhut.utils.CommonFunctions;
import com.seekerhut.utils.ConstValues;
import com.seekerhut.utils.JedisHelper;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/client")
public class AdminApiController extends BaseController {
    @Resource
    private BaseConfig baseConfig;

    public String gptApi = "https://api.chatanywhere.com.cn/v1/chat/completions";
    public String voiceApi = "";
    public HashMap<Integer, CharacterBean> characterMap = new HashMap<>();
    public HashMap<Long, DialogDataBean> dialogMap = new HashMap<>();
    public AdminApiController() {
    }

    @PostMapping("add_supported_characters")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "replyJson", value = "", paramType = "body", dataType = "String"),
    })
    public @ResponseBody String syncSupportedCharacters(String userToken, String name, String prompt) {
        try {
            var charBean = new CharacterBean(){
                {
                    characterId = 1;
                    characterName = name;
                    systemPrompt = prompt;
                }
            };
            JedisHelper.hset(ConstValues.RedisKeys.characterData, name, JSON.toJSONString(charBean));
            return Success("ok");
        }
        catch (Exception e) {
            return Fail(-1, "msg: " + e.getMessage() + ", stack: " + String.join("\n", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.toList())));
        }
    }
}
