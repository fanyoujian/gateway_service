package com.zxj.gtauth.tool;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 樊佑剑
 * @date 2020/7/8
 * lombok 代码简化工具 setter getter 可以替换手动创建 set get 方法
 */
@Setter
@Getter
@ToString
public class JsonResult<Data> implements Serializable {

    private int code;   //返回码 非0即失败
    private String msg; //消息提示
    private Map<String, Object> data; //返回的数据

    public JsonResult(){

    };

    public JsonResult(int code, String msg, Map<String, Object> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static String success() {
        return success(new HashMap(0));
    }
    public static String success(Map<String, Object> data) {
        return JSON.toJSONString(new JsonResult(0, "解析成功", data));
    }

    public static String failed() {
        return failed("解析失败");
    }
    public static String failed(String msg) {
        return failed(-1, msg);
    }
    public static String failed(int code, String msg) {
        return  JSON.toJSONString(new JsonResult(code, msg, new HashMap(0)));
    }
}
