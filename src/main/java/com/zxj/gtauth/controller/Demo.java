package com.zxj.gtauth.controller;

import com.zxj.gtauth.tool.Tool;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class Demo {

    @RequestMapping("hello")
    public String hello()
    {
        return "hello";
    }

    @RequestMapping("file")
    public String file() throws IOException {

        String saveFile = Tool.writeLog("cdcdcdcd",Tool.sysDayTime()+".txt");

        return  saveFile;
    }
}
