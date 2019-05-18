package com.leyou.auth.task;

import com.leyou.auth.config.JwtProperties;
import com.leyou.common.auth.utils.JwtUtils;
import com.leyou.privilege.entity.ApplicationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: 姜光明
 * @Date: 2019/5/18 11:31
 * <p>
 * 定时获取token，保存token
 */
@Slf4j
@Component
public class PrivilegeTokenHolder {
    @Autowired
    private JwtProperties prop;

    private String token;
    /**
     * token刷新间隔  24小时
     */
    private static final long TOKEN_REFRESH_INTERVAL = 86400000L;

    @Scheduled(fixedDelay = TOKEN_REFRESH_INTERVAL)
    public void loadToken() {
        try {
            //向ly-auth发起请求，获取JWT
            ApplicationInfo appInfo = new ApplicationInfo();
            appInfo.setId(prop.getApp().getId());
            token = JwtUtils.generateTokenExpireInSeconds(appInfo, prop.getPrivateKey(), prop.getApp().getExpire());
            log.info("【网关】定时获取token成功");
        } catch (Exception e) {
            log.info("【网关】定时获取token失败");
        }
    }

    public String getToken() {
        return token;
    }
}
