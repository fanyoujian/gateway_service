package com.zxj.gtauth.config;

import com.zxj.gtauth.filter.CustomerFilterFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.servlet.Filter;

@Configuration
/**
 * 自定义过滤器
 * fanyoujian
 */
public class CustomerFilterConfig {

    @Bean
    public CustomerFilterFactory customerFilterFactory() {

        return new CustomerFilterFactory();
    }
}
