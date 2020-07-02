package com.zxj.gtauth.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;

import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.alibaba.fastjson.JSONObject;


import java.util.*;

/**
 * 全局过滤器实现 token 认证，所有接口请求都必须经过 该过滤
 * auth 认证
 */
@Component
public class TokenFilter implements GlobalFilter, Ordered {

    private static String parseToken = null;
    private static  ServerHttpRequest host = null;
    private static  String aesToken = "";
    private static  String aesTimestamp = "";
    private static  String signature;
    private static  SortedMap<String,Object> sortedMap;
    private static JSONObject jsonObject;
    MultiValueMap<String, String> formData;

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_FORM_WWW_DATA = "application/x-www-form-urlencoded";
    private static final String CONTENT_TYPE_FORM_DATA = "form-data";

    private static final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    /**
     * 2cpLaV8VgKE8u1oM1HpNj77Oo9v_JIA_UB_XIE_mWzhcxpeybIlR70oJXZgz2zwAB4p_JIA_HgEggq7BweI_JIA_ZLiT_XIE_sjm298jo7nefR6200Mp6gQwWTpmG9_XIE_neaBghsZ0zrGuDuwddz5CQ75XQW5i8Tyq7qEJ_XIE_WuuNOxlQQ1jv9PKGT4NA0OrSyX_JIA_SfL_JIA_1XcnsawpNYmydzvGJTCWZyzap64z8DxflpJEvzFoBNN2xFViuHDCA5qXOiV_XIE_O2mdjZrdNQppqt6OQomkLEzUwpJ2p2TP51_XIE_ZKjcjzg_DENG__DENG_
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("==========filter token start=====");

        //获取 header 信息 token 进行解密 传输
//        String token = exchange.getRequest().getHeaders().getFirst("token");
//        System.out.println("==========filter token====="+token);
//
//        //token 为空 直接验证失败
//        if(token ==null)
//        {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//        //异常直接返回
//        try {
//            System.out.println("==========filter desEncrypt token=====");
//            //解密 token 对内容进行效验
//            token = Tool.specialReplaceParse(token);
//            parseToken = Aes.desEncrypt(token);
//
//            jsonObject = JSONObject.parseObject(parseToken);
//
//            if(jsonObject.getString("token") == null || jsonObject.getString("signature") == null)
//            {
//                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//                return exchange.getResponse().setComplete();
//            }
//            aesToken = jsonObject.getString("token").toString();
//            signature = jsonObject.getString("signature").toString();
//            if(jsonObject.getString("timestamp") != null)
//            {
//                aesTimestamp = jsonObject.getString("timestamp").toString();
//            }
//            System.out.println("==========jsonObject remove signature=====");
//            jsonObject.remove("signature");
//            //验证数据的合法性
//            Iterator iter = jsonObject.entrySet().iterator();
//            sortedMap = new TreeMap<String,Object>();
//            while (iter.hasNext()) {
//                Map.Entry entry = (Map.Entry) iter.next();
//                sortedMap.put(entry.getKey().toString(),entry.getValue().toString());
//            }
//            String authSign = Sign.createSign(Sign.paramSort(sortedMap));
//
//            System.out.println("==========jsonObject authSign====="+authSign);
//
//            if(!authSign.equals(signature))
//            {
//                System.out.println("========header token auth sign err====");
//                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//                return exchange.getResponse().setComplete();
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("========header token is null return false===="+e.getMessage());
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//        System.out.println("==========jsonObject aesToken====="+aesToken);
//        System.out.println("==========jsonObject aesTimestamp====="+aesTimestamp);
//        ServerHttpRequest headerInfo = exchange.getRequest().mutate()
//                .header("token", aesToken)
//                .header("timestamp",aesTimestamp)
//                .build();

//        ServerRequest serverRequest = new DefaultServerRequest(exchange);
        // mediaType
//        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
////        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
//        System.out.println("==========body mediaType========="+mediaType);

//        String method = exchange.getRequest().getMethodValue();
//        final String reqId = null ;
//
//        System.out.println("==========body method========="+method);

        //加入缓存
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    DataBufferUtils.retain(dataBuffer);
                    Flux<DataBuffer> cachedFlux = Flux
                            .defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
                    ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
                            exchange.getRequest()) {
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return cachedFlux;
                        }
                    };
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }


}
