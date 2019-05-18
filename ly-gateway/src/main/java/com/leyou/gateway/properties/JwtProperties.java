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
    private PublicKey publicKey; //公钥对象

    private UserTokenProperties user = new UserTokenProperties();  //用户token相关属性

    private PrivilegeTokenProperties app = new PrivilegeTokenProperties();  //服务token相关属性



    @Data
    public class UserTokenProperties{
        private String cookieName; //存放token的cookie名称
    }

    @Data
    public class PrivilegeTokenProperties{
        private Long id;  //服务id
        private String secret;   //服务密钥
    }

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
}
