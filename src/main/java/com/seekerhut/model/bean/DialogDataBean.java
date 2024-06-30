package com.seekerhut.model.bean;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class DialogDataBean {
    public String systemMessage;
    public long dialogPosition;
    public ArrayList<String> messageList;
    public DialogDataBean(String systemMsg) {
        systemMessage = systemMsg;
        dialogPosition = 0;
        messageList = new ArrayList<>();
    }
}