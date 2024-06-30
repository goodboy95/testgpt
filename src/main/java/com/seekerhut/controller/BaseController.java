package com.seekerhut.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class BaseController {
    public <T extends Serializable>T JObjectParse(JSONObject jobj) {
        return null;
    }

    protected String Success(String msg, Object data) {
        try {
            var dataStr = JSON.toJSONString(data);
            var jobj = new JSONObject();
            jobj.put("code", 0);
            jobj.put("message", msg);
            if (dataStr.substring(0, 1).equals("[")) jobj.put("data", JSONArray.parseArray(dataStr));
            else if (dataStr.substring(0, 1).equals("{")) jobj.put("data", JSONObject.parseObject(dataStr));
            else jobj.put("data", data);
            return jobj.toString();
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    protected String Success(Object data) {
        return Success("", data);
    }

    protected String Fail(int errCode, String msg) {
        try {
            var jobj = new JSONObject();
            jobj.put("code", errCode);
            jobj.put("message", msg);
            jobj.put("data", new JSONObject());
            return jobj.toString();
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    protected String Fail(String msg) {
        return Fail(-1, msg);
    }
}
