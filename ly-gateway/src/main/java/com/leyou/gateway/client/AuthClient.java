package com.leyou.gateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: 姜光明
 * @Date: 2019/5/18 11:08
 */
@FeignClient("auth-service")
public interface AuthClient {
    /**
     * 微服务认证并申请令牌
     *
     * @param id 服务id
     * @param secret 密码
     * @return token令牌
     */
    @GetMapping("authentication")
    String authenticate(@RequestParam("id") Long id, @RequestParam("secret") String secret);
}
