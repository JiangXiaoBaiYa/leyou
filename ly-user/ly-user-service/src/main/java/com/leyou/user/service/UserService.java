package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.user.entity.User;
import com.leyou.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 姜光明
 * @Date: 2019/5/14 22:58
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public Boolean checkUserData(String data, Integer type) {
        User u = new User();
        switch (type) {
            case 1:
                u.setUsername(data);
                break;
            case 2:
                u.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //返回布尔类型结果：true：可用  false：不可用
       return userMapper.selectCount(u)==0;
    }
}
