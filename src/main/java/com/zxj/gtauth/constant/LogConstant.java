package com.zxj.gtauth.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@PropertySource("classpath:constant.properties")
@ConfigurationProperties(prefix = "log",ignoreUnknownFields = true)
@Validated
/**
 * 常量注解 ,方便不通的环境 使用参数灵活配置
 * 樊佑剑 2020-07-08
 */
public class LogConstant {

    //注解是-为主 比如 ： log.log-dir=logs/
    private String logDir = "logs/";
    private String fileName;
    private String filePath;

    public String getLogDir() {
        return logDir;
    }

    public void setLogDir(String logDir) {
        this.logDir = logDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
