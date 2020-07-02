package com.zxj.gtauth.filter;

import com.alibaba.fastjson.JSONObject;
import com.zxj.gtauth.tool.Sign;
import com.zxj.gtauth.tool.Tool;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import io.netty.buffer.ByteBufAllocator;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义路由器 特定路由过滤
 */
public class CustomerFilterFactory extends AbstractGatewayFilterFactory<CustomerFilterFactory.Config> {
    private static final Logger log = LoggerFactory.getLogger( CustomerFilterFactory.class );
    private static final String COUNT_START_TIME = "countStartTime";
    private static final String CODE="code";
    private static final String PLATFORM = "platform";

    @Override
    public List<String> shortcutFieldOrder() {

        return Arrays.asList("enabled");
    }

    public CustomerFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            //请求参数 和 对应的 url 路径 写入日志
            ServerHttpRequest request = exchange.getRequest();
            System.out.println("URL : " + request.getURI());
            System.out.println("header : " + request.getHeaders());

            String param = "";
            if(request.getBody()!=null)
            {
                param = resolveBodyFromRequest((ServerHttpRequest) exchange.getRequest());
                System.out.println("==========body====="+ param);
            }
            String fileName = Tool.sysDayTime()+".txt";
            Tool.writeLog("http request url:"+request.getURI().toString(),fileName);
            Tool.writeLog("http request header:"+request.getHeaders().toString(),fileName);
            Tool.writeLog("http request body:"+param,fileName);

            return chain.filter(exchange);


//            System.out.println("============apply config isEnable=========="+config.isEnabled());
//
//            if (!config.isEnabled()) {
//                return chain.filter(exchange);
//            }
//            ServerHttpRequest serverHttpRequest = exchange.getRequest();
//            String method = serverHttpRequest.getMethodValue();
//            //get 方式获取参数
//            if("GET".equals(method))
//            {
//                MultiValueMap<String, String> parameterMap = exchange.getRequest().getQueryParams();
//                JSONObject jsonObject = new JSONObject();
//                Iterator iterMap = parameterMap.entrySet().iterator();
//                while(iterMap.hasNext()){
//                    Map.Entry strMap=(Map.Entry)iterMap.next();
//
//                    String strValue = strMap.getValue().toString();
//                    //判断第一个字符是否为“["
//                    if (strValue.startsWith("[")) {
//                        strValue = strValue.substring(1);
//                    }
//                    //判断最后一个字符是否为“]”
//                    if (strValue.endsWith("]")) {
//                        strValue = strValue.substring(0,strValue.length() - 1);
//                    }
//                    jsonObject.put(strMap.getKey().toString(),strValue);
//                }
//                System.out.println("========ParameterMap jsonObject=======");
//                System.out.println(jsonObject);
//
//                try {
//                    if(!validSign(jsonObject))
//                    {
//                        return responseCustomer(exchange.getResponse(),"验签失败,请核对信息是否正确");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            MediaType contentType = exchange.getRequest().getHeaders().getContentType();
//
//            System.out.println("=========contentType========"+contentType);
//            //post 比较麻烦
//            if("POST".equals(method))
//            {
//                if(contentType==null)return responseCustomer(exchange.getResponse(),"验签失败,请指定content-type");
//                //获取body 信息
//                String bodyStr = resolveBodyFromRequest(serverHttpRequest);
//                System.out.println("=======bodyStr=======");
//                System.out.println(bodyStr);
//                //根据 form-data or x-www-form-urlencode 进行解析
//                // x-www-form-urlencode
//                if(contentType.isCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED))
//                {
//                    try {
//                        if(!validSign(postUrlParam(bodyStr)))
//                        {
//                            return responseCustomer(exchange.getResponse(),"验签失败,请核对信息是否正确");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                //text/plain {"account":"15821619881","password":"123456"}
//                if(contentType.isCompatibleWith(MediaType.TEXT_PLAIN))
//                {
//                    try {
//                        if(!validSign(JSONObject.parseObject(bodyStr)))
//                        {
//                            return responseCustomer(exchange.getResponse(),"验签失败,请核对信息是否正确");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                //{"account":"15821619881","password":"123456"}
//                //application/json;charset=UTF-8 or application/json
//                if(contentType.isCompatibleWith(MediaType.APPLICATION_JSON) || contentType.isCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
//                {
//                    try {
//                        if(!validSign(JSONObject.parseObject(bodyStr)))
//                        {
//                            return responseCustomer(exchange.getResponse(),"验签失败,请核对信息是否正确");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("=========sortedMap application/json jsonObject========");
//                }
//                //multipart/form-data
//                if(contentType.isCompatibleWith(MediaType.MULTIPART_FORM_DATA))
//                {
//                    System.out.println("=========sortedMap multipart/form-data jsonObject========");
//                    System.out.println("=========sortedMap multipart/form-data account ========");
//
//                    System.out.println("=========sortedMap multipart/form-data bodyDataStr ========"+bodyStr);
//                    return responseCustomer(exchange.getResponse(),"暂时不支持 multipart/form-data 表单数据提交");
//                }
//                return chain.filter(exchange);
//            }
//            return chain.filter(exchange);
        };
    }
    private String formatStr(String str){
        if (str != null && str.length() > 0) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            return m.replaceAll("");
        }
        return str;
    }
    /**
     * 数据合法性效验
     * @return
     */
    private boolean validSign(JSONObject jsonObject) throws Exception {
        if(jsonObject.getString("signature")==null || jsonObject.getString("signature").equals(""))
        {
            System.out.println("==========validSign false=======");
            return false;
        }
        String signature = jsonObject.getString("signature");
        signature = Tool.specialReplace(signature);
        //移除 signature
        jsonObject.remove("signature");

        //根据key 排序
        SortedMap<String,Object> sortedMap = new TreeMap<>();
        Iterator iterMap = jsonObject.entrySet().iterator();
        while(iterMap.hasNext()){
            Map.Entry strMap=(Map.Entry)iterMap.next();
            sortedMap.put(strMap.getKey().toString(),strMap.getValue().toString());
        }
        String sortMap = Sign.paramSort(sortedMap);

        if(!Sign.createSign(sortMap).equals(signature))
        {
            System.out.println("==========validSign sign err false=======");
            return false;
        }

        return true;
    }
    public JSONObject  postUrlParam(String param) {

        JSONObject jsonObject = new JSONObject();
        String[] arr = param.split("&");
        for (String s : arr){
            String key = s.split("=")[0];
            String value = s.split("=")[1];
            jsonObject.put(key,value);
        }
        return jsonObject;
    }
    public static class Config {
        /**
         * 控制是否开启统计
         */
        private boolean enabled;

        public Config() {}

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * 从Flux<DataBuffer>中获取字符串的方法
     * @return 请求体
     */
    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        //获取请求体
//        Flux<DataBuffer> body = serverHttpRequest.getBody();
//
//        System.out.println("========resolveBodyFromRequest body====="+body);
//        System.out.println(body);
//
//        AtomicReference<String> bodyRef = new AtomicReference<>();
//        body.subscribe(buffer -> {
//            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
//            DataBufferUtils.release(buffer);
//            bodyRef.set(charBuffer.toString());
//        });
//        //获取request body
//        return bodyRef.get();

        //获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        StringBuilder sb = new StringBuilder();

        body.subscribe(buffer -> {
            byte[] bytes = new byte[buffer.readableByteCount()];
            buffer.read(bytes);
            DataBufferUtils.release(buffer);
            String bodyString = new String(bytes, StandardCharsets.UTF_8);
            sb.append(bodyString);
        });
        return sb.toString();
    }

    private DataBuffer stringBuffer(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }

    /**
     * 返回信息
     * @param response
     * @param msg
     * @return
     */
    protected Mono<Void> responseCustomer(ServerHttpResponse response,String msg)
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", msg);
        jsonObject.put("code",100);
        jsonObject.put("status",false);
        byte[] data = jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        DataBuffer buffer = response.bufferFactory().wrap(data);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}
