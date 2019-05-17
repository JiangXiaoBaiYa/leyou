package com.leyou.gateway.properties;

import com.leyou.common.auth.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.PublicKey;

/**
 * @Author: 姜光明
 * @Date: 2019/5/17 21:06
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties implements InitializingBean {
    private String pubKeyPath;  //公钥地址
    private PublicKey publicKey;

    private UserTokenProperties user = new UserTokenProperties();

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            //获取公钥和私钥
            publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("初始化公钥失败！", e);
            throw new RuntimeException(e);
        }

    }

    @Data
    public class UserTokenProperties{
        private String cookieName; //存放token的cookie名称
    }
}
