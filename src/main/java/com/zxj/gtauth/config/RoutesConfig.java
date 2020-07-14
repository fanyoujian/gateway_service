package com.zxj.gtauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Configuration
/**
 * 自定义路由实现 也可以根据 yml 配置文件实现
 */
public class RoutesConfig {

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {

        System.out.println("===========HiddenHttpMethodFilter start========");
        return new HiddenHttpMethodFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

                System.out.println("===========HiddenHttpMethodFilter chain.filter========");
                return chain.filter(exchange);
            }
        };
    }
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
