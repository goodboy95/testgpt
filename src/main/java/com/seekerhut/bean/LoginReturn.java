package com.seekerhut.bean;

public  class LoginReturn {
    int code;
    String msg;
    String loginToken;
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getLoginToken() {
        return loginToken;
    }
    public LoginReturn(int code, String msg, String loginToken) {
        this.code = code;
        this.msg = msg;
        this.loginToken = loginToken;
    }
}
