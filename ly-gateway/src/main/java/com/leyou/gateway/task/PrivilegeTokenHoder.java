package com.leyou.gateway.task;

import com.leyou.gateway.client.AuthClient;
import com.leyou.gateway.properties.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: 姜光明
 * @Date: 2019/5/18 11:11
 * <p>
 * 定时获取token，保存token
 */
@Slf4j
@Component
public class PrivilegeTokenHoder {

    @Autowired
    private JwtProperties prop;

    private String token;
    /**
     * token刷新间隔，因token有效期为25小时，所以设置刷新间隔24小时
     */
    private static final long TOKEN_REFRESH_INTERVAL = 86400000L;
    /**
     * token获取失败后重试的间隔 10秒
     */
    private static final long TOKEN_RETRY_INTERVAL = 10000L;

    @Autowired
    private AuthClient authClient;

    @Scheduled(fixedDelay = TOKEN_REFRESH_INTERVAL)
    public void loadToken() throws InterruptedException {
        while (true) {
            try {
                //向ly-auth发起请求，获取JWT
                token = authClient.authenticate(prop.getApp().getId(), prop.getApp().getSecret());
                log.info("【网关】定时获取token成功");
                break;
            } catch (Exception e) {
                log.info("【网关】定时获取token失败");
            }
            //休眠10秒，再次重试
            Thread.sleep(TOKEN_RETRY_INTERVAL);
        }
    }

    public String getToken() {
        return token;
    }

}
