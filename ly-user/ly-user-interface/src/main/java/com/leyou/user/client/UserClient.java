package com.leyou.user.client;

import com.leyou.item.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: 姜光明
 * @Date: 2019/5/15 21:45
 */
@FeignClient("user-service")
public interface UserClient {

    /**
     * 根据用户名和密码查询用户
     * @param username 用户名
     * @param password 密码
     * @return 用户DTO
     */
    @GetMapping("/query")
    UserDTO queryUserByUsernameAndPassword(@RequestParam("username")String username,
                                          @RequestParam("password")String password);
}
