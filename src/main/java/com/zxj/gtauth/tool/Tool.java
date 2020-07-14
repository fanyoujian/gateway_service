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
    public static void writeLog(String str,String fileName) throws IOException {


        String line = System.getProperty("line.separator");

        System.out.println(fileName);
        File file = new File(fileName);

        if(!file.exists())
        {

            System.out.println("===============writeLog is not=========");
            file.createNewFile();
        }

        OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(file,true), "utf-8");
        oStreamWriter.append(str+line);
        oStreamWriter.close();
    }

    public static void writeDirLog(String str, String fileName, String dir) throws IOException {

        File fileDir = new File(dir);
        if(!fileDir.exists() && !fileDir.isDirectory())
        {
            System.out.println("===============writeLog is not=========");
            fileDir.mkdirs();
        }
        writeLog(str,dir+fileName);
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
