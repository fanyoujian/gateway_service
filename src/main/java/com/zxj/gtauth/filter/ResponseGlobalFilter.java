package com.zxj.gtauth.filter;


import com.alibaba.fastjson.JSONObject;
import com.zxj.gtauth.tool.Tool;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * 后置过滤器 对 response data 数据进行加密 返回
 * 响应
 */
@Component
public class ResponseGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
         @Override
         public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
            if (getStatusCode().equals(HttpStatus.OK) && body instanceof Flux) {
              Flux<? extends DataBuffer> fluxBody = Flux.from(body);

              return super.writeWith(fluxBody.map(dataBuffer -> {
                  //获取原始返回参数
                  byte[] content = new byte[dataBuffer.readableByteCount()];
                  dataBuffer.read(content);
                  //释放掉内存
                  DataBufferUtils.release(dataBuffer);
                  //responseData就是下游系统返回的内容,可以查看修改 必须是json 格式
                  String responseData = new String(content, Charset.forName("UTF-8"));

                  Tool.writeLog("http response : "+responseData,Tool.sysDayTime()+".txt");

                  System.out.println("======响应内容: jsonObject responseData===="+responseData);

                  JSONObject object = JSONObject.parseObject(responseData);
//                  // 获取对应的 data 数据 进行加密 返回
                  String data = object.getString("data");
                  System.out.println("======响应内容: jsonObject data===="+data);
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
//
                  byte[] newRs = object.toString().getBytes(Charset.forName("UTF-8"));
//                  //重新指定字节长度 很重要
                  this.getDelegate().getHeaders().setContentLength(newRs.length);
                  return bufferFactory.wrap(newRs);
//                  return bufferFactory.wrap("".getBytes());
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
