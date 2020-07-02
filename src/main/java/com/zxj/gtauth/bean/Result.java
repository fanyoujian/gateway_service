package com.zxj.signauth.bean;

public class Result {
    //状态码
    private String code;
    //描述信息
    private String msg;
    //返回数据
    private String data;
    //加密信息
    private String cbc;

    public Result(String code, String msg, String data, String cbc) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.cbc = cbc;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Result() {
    }
    public Result(String cbc) {
        this.cbc = cbc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCbc() {
        return cbc;
    }

    public Result(String code, String msg, String cbc) {
        this.code = code;
        this.msg = msg;
        this.cbc = cbc;
    }

    public void setCbc(String cbc) {
        this.cbc = cbc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
