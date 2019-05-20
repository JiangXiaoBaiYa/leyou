package com.leyou.order.interceptor;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.common.threadlocals.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: 姜光明
 * @Date: 2019/5/20 9:49
 * 定义SpringMVC的拦截器，在拦截器中获取用户信息，并保存到ThreadLocal中
 */
public class UserInterceptor implements HandlerInterceptor {

    private static final String HEADER_NAME = "user_info";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头
        String header = request.getHeader(HEADER_NAME);
        if (StringUtils.isBlank(header)) {
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
        //保存用户信息
        UserHolder.setUser(Long.valueOf(header));
        return true;
    }
}
