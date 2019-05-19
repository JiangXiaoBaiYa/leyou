package com.leyou;

import com.leyou.privilege.config.JwtProperties;
import com.leyou.privilege.config.PasswordConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: 姜光明
 * @Date: 2019/5/18 9:18
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.leyou.privilege.mapper")
@EnableConfigurationProperties({PasswordConfig.class, JwtProperties.class})
public class LyPrivilegeApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyPrivilegeApplication.class, args);
    }
}
