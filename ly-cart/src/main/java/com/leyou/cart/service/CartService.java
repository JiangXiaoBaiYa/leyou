package com.leyou.cart.service;

import com.leyou.cart.entity.Cart;
import com.leyou.common.threadlocals.UserHolder;
import com.leyou.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: 姜光明
 * @Date: 2019/5/20 10:07
 */
@Service
public class CartService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "ly:cart:uid:";

    public void addCart(Cart cart) {
        //获取当前用户信息
        Long uid = UserHolder.getUser();
        String key = KEY_PREFIX + uid;
        //获取hash操作的对象，并绑定用户id
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
        //获取商品id，作为hashKey
        String hashKey = cart.getSkuId().toString();
        //获取数量
        int skuNum = cart.getNum();
        //判断要添加的商品是否存在
        Boolean boo = hashOps.hasKey(hashKey);

        if (boo != null && boo) {
            //存在，修改数量
            cart = JsonUtils.toBean(hashOps.get(hashKey), Cart.class);
            cart.setNum(skuNum + cart.getNum());
        }
        //写入redis
        hashOps.put(hashKey,JsonUtils.toString(cart));
    }
}
