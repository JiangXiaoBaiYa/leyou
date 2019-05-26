package com.leyou.user.service;

import com.leyou.common.constants.MQConstants;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.common.utils.BeanHelper;
import com.leyou.common.utils.RegexUtils;
import com.leyou.user.dto.UserDTO;
import com.leyou.user.entity.User;
import com.leyou.user.mapper.UserMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;

    public static final String KEY_PREFIX = "VERIFY_CODE:";

    public void sendCode(String phone) {
        //使用自定义工具类验证手机号码
        if (!RegexUtils.isPhone(phone)) {
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //使用工具生成6位随机数字
        String randomNumeric = RandomStringUtils.randomNumeric(6);

        //把随机数放入redis缓存中，设置失效时间5分钟
        redisTemplate.opsForValue().set(KEY_PREFIX+phone,randomNumeric,5, TimeUnit.MINUTES);

        //发送RabbitMQ消息到ly-sms
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", randomNumeric);
        amqpTemplate.convertAndSend(MQConstants.Exchange.SMS_EXCHANGE_NAME,MQConstants.RoutingKey.VERIFY_CODE_KEY,map);
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    /**
     * 用户注册功能
     * @param user
     * @param code
     */
    public void register(User user, String code) {
        //1.校验短信验证码
        // 1.1从redis中取出验证码
        String record = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        //1.2比较验证码
        if (!StringUtils.equals(code, record)) {
            throw new LyException(ExceptionEnum.INVALID_VERIFY_CODE);
        }
        //2.对密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
       //写入数据库
        int count = userMapper.insertSelective(user);
        if (count != 1) {
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

    }

    public UserDTO queryUserByUsernameAndPassword(String username, String password) {
        //1.根据用户名查询(因username有索引，而password没有索引，一起查的话索引失效，表会全表扫描，性能下降)
        User u = new User();
        u.setUsername(username);
        User user = userMapper.selectOne(u);
        //2.判断用户是否存在
        if (user == null) {
            //用户名错误
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        boolean b = passwordEncoder.matches(password, user.getPassword());
        if (!b) {
            //密码错误
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        return BeanHelper.copyProperties(user, UserDTO.class);
    }
}
