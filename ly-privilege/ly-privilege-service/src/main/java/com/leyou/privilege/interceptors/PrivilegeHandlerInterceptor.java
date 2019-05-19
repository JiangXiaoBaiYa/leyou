package com.leyou.privilege.interceptors;

import com.leyou.common.auth.entity.Payload;
import com.leyou.common.auth.utils.JwtUtils;
import com.leyou.privilege.config.JwtProperties;
import com.leyou.privilege.entity.ApplicationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 校验请求头中的token
 *
 * @Author: 姜光明
 * @Date: 2019/5/19 15:45
 */
@Slf4j
public class PrivilegeHandlerInterceptor implements HandlerInterceptor {

    private JwtProperties jwtProp;

    public PrivilegeHandlerInterceptor(JwtProperties jwtProp) {
        this.jwtProp = jwtProp;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            //获取请求头
            String token = request.getHeader(jwtProp.getPrivilege().getHeaderName());
            //校验
            Payload<ApplicationInfo> payload = JwtUtils.getInfoFromToken(token, jwtProp.getPublicKey(), ApplicationInfo.class);
            //TODO 获取token中的服务信息，做细粒度认证
            ApplicationInfo userInfo = payload.getUserInfo();
            log.info("服务{}正在请求资源：{}",userInfo.getServiceName(),request.getRequestURI());
            return true;
        } catch (Exception e) {
            log.error("服务访问被拒绝,token认证失败!", e);
            return false;
        }
    }
}
