package com.zxj.gtauth.bean;

/**
 * auth 认证实体类
 */
public class AuthCode {

    //授权平台 iOS Android
    private String platform;
    //授权码
    private String code;

    private String msg;

    public AuthCode() {
    }
    public AuthCode(String platform, String code) {
        this.platform = platform;
        this.code = code;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "{\"platform\"=\"" + platform + "\"" +
                ", \"code=\"" + code + "\"" +
                "}" ;
    }
}
