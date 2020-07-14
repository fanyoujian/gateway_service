package com.zxj.gtauth.controller;

import com.zxj.gtauth.constant.LogConstant;
import com.zxj.gtauth.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
//@Component
public class Demo {


    private static final String LogDir = "logs/";

    @Autowired
    private LogConstant lC;

    @RequestMapping("hello")
    public String hello()
    {
        return "hello";
    }

    @RequestMapping("file")
    public String file() {
        System.out.println("======demo file======="+lC.getLogDir());
       try{
         Tool.writeDirLog("ddddd",Tool.sysDayTime()+".txt",lC.getLogDir());
//
           return  "";
       }catch (Exception e)
       {
           System.out.println("======demo file try catch======="+e.getMessage());
           return e.getMessage();
       }
    }
    @RequestMapping("log")
    public String logProperties()
    {
        return "dev logs 121331 dev";
//        LogConstant lC = new LogConstant();
//        return lC.getLogDir();
    }
}
