package com.leyou.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: 姜光明
 * @Date: 2019/5/14 20:30
 */
@Data
@ConfigurationProperties(prefix = "ly.sms")
public class SmsProperties {
    /**
     * 账号
     */
    private String accessKeyID;
    /**
     * 密钥
     */
    private String accessKeySecret;
    /**
     * 短信签名
     */
    private String signName;
    /**
     * 短信模板
     */
    private String verifyCodeTemplate;
    /**
     * 发送短信请求的域名
     */
    private String domain;
    /**
     * API版本
     */
    private String version;
    /**
     * API类型
     */
    private String action;
    /**
     * 区域
     */
    private String regionID;
}
