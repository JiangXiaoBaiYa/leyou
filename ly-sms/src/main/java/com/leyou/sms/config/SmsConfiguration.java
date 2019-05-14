package com.leyou.sms.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 姜光明
 * @Date: 2019/5/14 20:30
 */
@Configuration
@EnableConfigurationProperties(value = SmsProperties.class)
public class SmsConfiguration {
    @Bean
    public IAcsClient acsClient(SmsProperties prop) {
        DefaultProfile profile = DefaultProfile.getProfile(prop.getRegionID(), prop.getAccessKeyID(), prop.getAccessKeySecret());
        return new DefaultAcsClient(profile);
    }
}
