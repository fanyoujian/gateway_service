package com.zxj.gtauth.exception;

import com.zxj.gtauth.tool.Tool;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

/**
 * @Deacription TODO
 * @Author fanyoujian
 * @Date 2020/7/2
 * gateway 服务异常处理 抽象类
 **/
public abstract class AbstractExceptionHandler {

    private static final String DEFAULT_ERROR_CODE = "999999";

    protected String formatMessage(Throwable ex) {
        String errorMessage = null;
        if (ex instanceof NotFoundException) {
            String reason = ((NotFoundException) ex).getMessage();
            errorMessage = reason;
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            errorMessage = responseStatusException.getMessage();
        } else {
            errorMessage = ex.getMessage();
        }
        return errorMessage;
    }

    /**
     *  "code": 1,
     *     "msg": "登录成功",
     *     "time": "1593653304",
     *     "data": {
     * @param errorMessage
     * @return
     */
    protected Map<String, Object> buildErrorMap(String errorMessage) {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("success", false);
        resMap.put("code", DEFAULT_ERROR_CODE);
        resMap.put("msg", errorMessage);
        resMap.put("data", null);
        resMap.put("time", Tool.sysTimestamp());
        return resMap;
    }

}