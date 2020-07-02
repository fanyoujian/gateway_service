package com.zxj.gtauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * 自定义路由实现 也可以根据 yml 配置文件实现
 */
public class RoutesConfig {
//    @Autowired
//    private KeyResolver addressKeyResolver;
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                //权重路由
//                .route(r->r.path("/test/weight").
//                        filters(f -> f.retry(3).requestRateLimiter().
//                                rateLimiter(RedisRateLimiter.class, config -> config.setBurstCapacity(1).
//                                        setReplenishRate(1)).configure(config ->
//                                config.setKeyResolver(addressKeyResolver)).stripPrefix(1))
//                                .uri("lb://service-hi"))
//                  //集成了可能会用到的各种filter
//                 .build();
//    }

}
