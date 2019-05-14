package com.leyou.user.mapper;

import com.leyou.user.entity.User;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author: 姜光明
 * @Date: 2019/5/14 22:55
 */
public interface UserMapper extends Mapper<User> , IdListMapper<User,Long> {
}
