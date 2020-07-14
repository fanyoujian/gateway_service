package com.zxj.gtauth.filter;

import io.netty.buffer.ByteBufAllocator;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;

import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.alibaba.fastjson.JSONObject;


import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 全局过滤器实现 请求体加入缓存-为后续过滤效验做基础，所有接口请求都必须经过 该过滤
 * auth 认证
 */
@Component
public class GlobalCacheFilter implements GlobalFilter, Ordered {

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

        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();

        if (httpHeaders.getContentType() == null || httpHeaders.getContentLength()<1) {
            System.out.println("=========token filter start is null============");
            return chain.filter(exchange);
        } else {
            System.out.println("=========token filter start is not null============");
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


        //获取 header 信息 token 进行解密 传输
//        String token = exchange.getRequest().getHeaders().getFirst("token");
//        System.out.println("==========filter token====="+token);
//        return chain.filter(exchange);
        //token 为空 直接验证失败
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

//        System.out.println("==========filter DataBufferUtils.join cache =====");
//        System.out.println(Tool.sysTimestamp());
//        //加入缓存

//        if(method.equals("GET"))
//        {
//            System.out.println("===========get start============");
//            return chain.filter(exchange);
//        }
//        return DataBufferUtils.join(exchange.getRequest().getBody())
//                .flatMap(dataBuffer -> {
//                    DataBufferUtils.retain(dataBuffer);
//                    Flux<DataBuffer> cachedFlux = Flux
//                            .defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
//                    ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
//                            exchange.getRequest()) {
//                        @Override
//                        public Flux<DataBuffer> getBody() {
//                            return cachedFlux;
//                        }
//                    };
//                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
//                });


//        URI uri = exchange.getRequest().getURI();
//        URI ex = UriComponentsBuilder.fromUri(uri).build(true).toUri();
//        ServerHttpRequest request = exchange.getRequest().mutate().uri(ex).build();
//        if("POST".equalsIgnoreCase(request.getMethodValue())){//判断是否为POST请求
//            Flux<DataBuffer> body = request.getBody();
//            AtomicReference<String> bodyRef = new AtomicReference<>();//缓存读取的request body信息
//            body.subscribe(dataBuffer -> {
//                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer());
//                DataBufferUtils.release(dataBuffer);
//                bodyRef.set(charBuffer.toString());
//            });//读取request body到缓存
//            String bodyStr = bodyRef.get();//获取request body
//            System.out.println(bodyStr);//这里是我们需要做的操作
//            DataBuffer bodyDataBuffer = stringBuffer(bodyStr);
//            Flux<DataBuffer> bodyFlux = Flux.just(bodyDataBuffer);
//
//            request = new ServerHttpRequestDecorator(request){
//                @Override
//                public Flux<DataBuffer> getBody() {
//                    return bodyFlux;
//                }
//            };//封装我们的request
//        }
//        return chain.filter(exchange.mutate().request(request).build());
    }

    private ServerWebExchange requestExchange(ServerWebExchange exchange) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        URI requestUri = serverHttpRequest.getURI();
        URI ex = UriComponentsBuilder.fromUri(requestUri).build(true).toUri();
        ServerHttpRequest newRequest = serverHttpRequest.mutate().uri(ex).build();

        // 获取body内容
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        StringBuilder sb = new StringBuilder();
        body.subscribe(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            String bodyString = new String(bytes, StandardCharsets.UTF_8);
            sb.append(bodyString);
        });

        String bodyStr = sb.toString();
        System.out.println("=============jsonObject bodyStr======"+bodyStr);
        if(bodyStr==null||bodyStr.equals(""))
        {

            JSONObject jsonObject = JSONObject.parseObject("{}");
            jsonObject.put("source_platform", "gateway");
            bodyStr = jsonObject.toJSONString();
        }

        System.out.println("=============jsonObject bodyStr new ======"+bodyStr);
//        System.out.println(jsonObject);
        //转成字节
        byte[] bytes = bodyStr.getBytes(StandardCharsets.UTF_8);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer bodyDataBuffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        bodyDataBuffer.write(bytes);

        Flux<DataBuffer> bodyFlux = Flux.just(bodyDataBuffer);
        newRequest = new ServerHttpRequestDecorator(newRequest) {
            @Override
            public Flux<DataBuffer> getBody() {
                return bodyFlux;
            }
        };
        return exchange.mutate().request(newRequest).build();
    }
    private DataBuffer stringBuffer(String value){
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }

//    protected DataBuffer stringBuffer(String value) {
//        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
//
//        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
//        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
//        buffer.write(bytes);
//        return buffer;
//    }
    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
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
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }


}
