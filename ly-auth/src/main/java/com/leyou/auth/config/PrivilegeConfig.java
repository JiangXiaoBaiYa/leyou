package com.leyou.auth.config;

import com.leyou.auth.task.PrivilegeTokenHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 姜光明
 * @Date: 2019/5/19 15:25
 */
@Slf4j
@Configuration
public class PrivilegeConfig {

    @Bean
    public RequestInterceptor requestInterceptor(JwtProperties prop, PrivilegeTokenHolder tokenHolder) {
        return requestTemplate -> {
            log.info("添加请求验证token!");
            requestTemplate.header(prop.getApp().getHeaderName(), tokenHolder.getToken());
        };
    }
}
