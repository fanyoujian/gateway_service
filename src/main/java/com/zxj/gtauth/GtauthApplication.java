package com.zxj.gtauth;

import com.zxj.gtauth.constant.LogConstant;
import com.zxj.gtauth.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GtauthApplication {
    @Autowired
    private static LogConstant lC;

    public static void main(String[] args) {

        SpringApplication.run(GtauthApplication.class, args);

        System.out.println("=====dev test new=======");
//        Tool.writeDirLog("=======first save log=====","debug.txt",lC.getLogDir());
    }

}
