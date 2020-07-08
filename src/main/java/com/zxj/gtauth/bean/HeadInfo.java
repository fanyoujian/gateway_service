package com.zxj.gtauth.bean;

/**
 * Header 传输实体类
 * 请求 header 信息
 */
public class HeadInfo {

    protected String token;

    public HeadInfo(String token) {
        this.token = token;
    }

    public HeadInfo() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
