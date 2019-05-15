package com.leyou.user.controller;

import com.leyou.common.exceptions.LyException;
import com.leyou.item.dto.UserDTO;
import com.leyou.user.entity.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * @Author: 姜光明
 * @Date: 2019/5/14 23:02
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 检验数据是否可用
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data, @PathVariable("type") Integer type) {
        return ResponseEntity.ok(userService.checkUserData(data, type));
    }

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    @PostMapping("/code")
    public ResponseEntity<Void> sendCode(@RequestParam("phone") String phone) {
        userService.sendCode(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 用户注册功能
     *
     * @param userDTO
     * @param code
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid User user, BindingResult result, @RequestParam("code") String code) {
        //我们在User参数后面跟一个BindingResult参数，不管校验是否通过，都会进入方法内部
        //BindingResult中会封装错误结果，我们通过result.hashErrors来判断是否有错误，然后用result.getFieldErrors来获取错误信息。
        if (result.hasErrors()) {
            String msg = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining("|"));
            throw new LyException(400, msg);
        }

        userService.register(user, code);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户名和密码查询用户
     * @param username 用户名
     * @param password 密码
     * @return 用户DTO
     */
    @GetMapping("/query")
    public ResponseEntity<UserDTO> queryUserByUsernameAndPassword(@RequestParam("username")String username,@RequestParam("password")String password) {
        return ResponseEntity.ok(userService.queryUserByUsernameAndPassword(username, password));
    }
}
