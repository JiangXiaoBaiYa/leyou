package com.leyou.common.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 姜光明
 * @Date: 2019/5/15 20:39
 * 载荷对象中的用户信息（假设目前包含三部分）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private Long id;   //用户id

    private String username;  //用户名

    private String role;   //角色（权限中会使用）
}
