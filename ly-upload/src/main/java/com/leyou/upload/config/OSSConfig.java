package com.leyou.upload.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 姜光明
 * @Date: 2019/5/5 11:18
 */
@Configuration
public class OSSConfig {
    @Bean
    public OSS ossClient(OSSProperties prop) {
        OSS oss = new OSSClientBuilder().build(prop.getEndpoint(), prop.getAccessKeyId(), prop.getAccessKeySecret());
        return oss;
    }
}
