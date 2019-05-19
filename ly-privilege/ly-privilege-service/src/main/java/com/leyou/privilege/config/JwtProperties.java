package com.leyou.privilege.config;

import com.leyou.common.auth.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.PublicKey;

/**
 * @Author: 姜光明
 * @Date: 2019/5/19 15:37
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties implements InitializingBean {

    private String pubKeyPath; //公钥地址

    private PublicKey publicKey; //公钥对象

    private PrivilegeTokenProperties privilege = new PrivilegeTokenProperties();
    @Data
    public class PrivilegeTokenProperties {
        private Long id;//服务id
        private String secret; //服务密钥
        private String headerName; //存放服务认证token的请求头
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            //获取公钥
            publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("初始化公钥失败！", e);
            throw new RuntimeException(e);
        }
    }
}
