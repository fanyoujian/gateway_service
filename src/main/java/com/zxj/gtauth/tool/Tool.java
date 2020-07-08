package com.zxj.gtauth.tool;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedMap;

/**
 * 工具类
 */
public class Tool {

    private static String specialStr;

    public static String escapeExprSpecialWord(String keyword) {
        if (StringUtils.isNotEmpty(keyword)) {
            String[] fbsArr = { "\\","$","(",")","*","+",".","[", "]","?","^","{","}","|","'","%" };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }

    public static String  specialReplace(String str)
    {
        specialStr = str.replaceAll("\\+","_JIA_");
        specialStr = specialStr.replaceAll("\\/","_XIE_");
        specialStr = specialStr.replaceAll("\\-","_JIAN_");
        specialStr = specialStr.replaceAll("\\*","_XING_");
        specialStr = specialStr.replaceAll("\\=","_DENG_");
        specialStr = specialStr.replaceAll("\\ ","_KONG_");
        return reolaceKongHang(specialStr);
    }

    public static String  specialReplaceParse(String str)
    {
        specialStr = str.replaceAll("_JIA_","+");
        specialStr = specialStr.replaceAll("_XIE_","/");
        specialStr = specialStr.replaceAll("_JIAN_","-");
        specialStr = specialStr.replaceAll("_XING_","*");
        specialStr = specialStr.replaceAll("_KONG_"," ");
        specialStr = specialStr.replaceAll("_DENG_","=");
        return reolaceKongHang(specialStr);
    }
    /**
     * 删除map 对应的key 内容
     * @param paramsMap
     * @param key
     */
    public static void deleteKeyOfMap(SortedMap<Object, Object> paramsMap, String key){
        Iterator<Object> iter = paramsMap.keySet().iterator();
        while(iter.hasNext()){
            if(key.equals(iter.next())){
                iter.remove();
            }
        }

    }

    public static String reolaceKongHang(String str)
    {
        str = str.replaceAll(" ","");
        str = str.replaceAll("\r|\n","");
        return str;
    }

    /**
     * 追加的形式 写入文件
     * @param str
     * @param fileName
     * @throws IOException
     */
    public static String writeLog(String str,String fileName) {

       try{
           String line = System.getProperty("line.separator");

           File file = new File(fileName);
           if(!file.exists())file.createNewFile();

           OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(file,true), "utf-8");
           oStreamWriter.append(str+line);
           oStreamWriter.close();

//           FileOutputStream fos = new FileOutputStream(file,true);  // true:表示追加。  默认覆盖
//           fos.write((str+line).getBytes());  // \r\n 表示换行。
//           fos.close();

//           Writer writer = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
//
//           writer.write(str+line);
//           FileWriter fileWriter = new FileWriter(file.getName(),true);
//           fileWriter.write(str+line);
//
//           fileWriter.close();
       }catch (Exception e)
       {
           return e.getMessage();
       }
       return "success";
    }

    /**
     * 获取系统时间 精确到当天
     * @return
     */
    public static String sysDayTime()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        return df.format(new Date()).toString();
    }

    /**
     * 获取系统时间戳
     * @return
     */
    public static long sysTimestamp()
    {
        return new Date().getTime()/1000;
    }
}
