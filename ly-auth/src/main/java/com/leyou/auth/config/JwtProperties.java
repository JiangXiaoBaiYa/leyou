package com.leyou.auth.config;

import com.leyou.common.auth.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author: 姜光明
 * @Date: 2019/5/15 21:20
 *
 * 编写属性类加载公钥私钥地址
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties implements InitializingBean {
    private String pubKeyPath; //公钥地址
    private String priKeyPath;  //私钥地址


    private PublicKey publicKey;
    private PrivateKey privateKey;

    private UserTokenPropperties user = new UserTokenPropperties(); //用户token相关属性
    @Data
    public class UserTokenPropperties{
        private int expire;  //token过期时常
        private String cookieName; //存放token的cookie名称
        private String cookieDomain;//存放token的cookie的domain
        private int minRefreshInterval; //cookie的最小刷新间隔
    }

    /**
     * 在bean注入到springIOC容器后立马加载此方法
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            //获取公钥私钥
            publicKey = RsaUtils.getPublicKey(pubKeyPath);
            privateKey = RsaUtils.getPrivateKey(priKeyPath);
        } catch (Exception e) {
            log.error("初始化公钥和私钥失败！", e);
            throw new RuntimeException(e);
        }
    }
}
