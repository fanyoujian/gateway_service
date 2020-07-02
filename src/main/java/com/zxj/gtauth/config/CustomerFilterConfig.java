package com.zxj.gtauth.config;

import com.zxj.gtauth.filter.CustomerFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
