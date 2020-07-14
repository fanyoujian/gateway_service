package com.zxj.gtauth.filter;


import com.alibaba.fastjson.JSONObject;
import com.zxj.gtauth.constant.LogConstant;
import com.zxj.gtauth.tool.Tool;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 后置过滤器 对 response data 数据进行加密 返回
 * 响应
 */
@Component
public class ResponseGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private LogConstant lC;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();

        System.out.println("=====ResponseGlobalFilter headerList start ======");



        System.out.println(originalResponse);
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
         @Override
         public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

             System.out.println("=====ResponseGlobalFilter headerList======");
             List<String> headerContentTypeList = originalResponse.getHeaders().get(HttpHeaders.CONTENT_TYPE);
             System.out.println(headerContentTypeList);

             boolean isJson = false;
             System.out.println("=====ResponseGlobalFilter isJson======"+isJson);

             for (String s : headerContentTypeList)
             {
                 if(s.contains("application/json"))
                 {
                     isJson = true;
                     break;
                 }
             }
             System.out.println("=====ResponseGlobalFilter isJson======"+isJson);
             if(!isJson)
             {
                 return super.writeWith(body);
             }

             if (getStatusCode().equals(HttpStatus.OK) && body instanceof Flux) {
              Flux<? extends DataBuffer> fluxBody = Flux.from(body);

              //fluxBody.buffer()
              return super.writeWith(fluxBody.buffer().map(dataBuffer -> {

                  DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                  DataBuffer join = dataBufferFactory.join(dataBuffer);
                  byte[] content = new byte[join.readableByteCount()];
                  join.read(content);
                  //释放掉内存
                  DataBufferUtils.release(join);

                  //responseData就是下游系统返回的内容,可以查看修改 必须是json 格式
                  String responseData = new String(content, Charset.forName("UTF-8"));
                  System.out.println("======响应内容: jsonObject responseData===="+responseData);

                  try {
                      Tool.writeDirLog("http response : "+responseData,Tool.sysDayTime()+".txt",lC.getLogDir());
                  } catch (IOException e) {
                      System.out.println("======响应内容: jsonObject responseData try catch 222 ====");
                  }
                  // 获取对应的 data 数据 进行加密 返回
                  JSONObject object = JSONObject.parseObject(responseData);
//                  String data = object.getString("data");
//                  System.out.println("======响应内容: jsonObject data===="+data);
//
//                  String cbsToken = null;
//                  if(data != null)
//                  {
//                      System.out.println("======响应内容: jsonObject data===="+data);
//                      try {
//                          cbsToken = Aes.encrypt(data);
//                          object.put("data",cbsToken);
//                      } catch (JsonProcessingException e) {
//                          e.printStackTrace();
//                      } catch (Exception e) {
//                          e.printStackTrace();
//                      }
//                  }
                  System.out.println("======响应内容: jsonObject responseData utf8====");
                  //重新指定字节长度 很重要
                  byte[] newRs = object.toString().getBytes(Charset.forName("UTF-8"));
                  this.getDelegate().getHeaders().setContentLength(newRs.length);

                  System.out.println("======响应内容: jsonObject responseData return newRs.length===="+newRs.length);

                  System.out.println("======响应内容: jsonObject responseData return newRs.length end ===="+this.getDelegate().getHeaders().getContentLength());


                  return bufferFactory.wrap(newRs);
              }));
            }else{
                System.out.println("======响应code异常:{}===="+getStatusCode());
            }
            return super.writeWith(body);
          }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
//        return Ordered.HIGHEST_PRECEDENCE;
        //-1 response 返回结果级别 -2 在返回之前处理
        return -2;
    }
}
