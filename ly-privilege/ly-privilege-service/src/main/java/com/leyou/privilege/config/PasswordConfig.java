package com.leyou.privilege.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

/**
 * @Author: 姜光明
 * @Date: 2019/5/15 10:24
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ly.encoder.crypt")
public class PasswordConfig {
    private int strength;
    private String secret;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        //利用密钥生成随机安全吗
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
        //初始化BCryptPasswordEncoder
        return new BCryptPasswordEncoder(strength, secureRandom);
    }
}
