package com.zxj.gtauth.tool;


import java.util.*;

/**
 * 验签算法
 */
public class Sign {

    public static final String PARAM_EQUAL = "=";

    public static final String PARAM_AND = "&";

    /**
     * 所有参与传参的参数按照accsii排序（升序）
     * 1 为空的内容过滤
     * 2 根据 key 对应的 accsii 升序 排序
     * @param parameters
     * @return
     */
    public static String paramSort(SortedMap<String, Object> parameters){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            //空值不传递，不参与签名组串
            if(null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }
        //删除多余的最后一个字符
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 根据param 获取对应的 sign
     * param 进行 cbc 加密 获取 sign
     * @param param
     * @return
     */
    public static String createSign(String param) throws Exception {

        return Tool.specialReplace(Aes.encrypt(param));
    }

}
