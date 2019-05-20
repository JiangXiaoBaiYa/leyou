package com.leyou.cart.service;

import com.leyou.cart.entity.Cart;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.common.threadlocals.UserHolder;
import com.leyou.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 姜光明
 * @Date: 2019/5/20 10:07
 */
@Slf4j
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

    public List<Cart> queryCartList() {
        //获取登录用户
        String key = KEY_PREFIX + UserHolder.getUser();
        //判断是否存在购物车
        Boolean boo = redisTemplate.hasKey(key);
        if (boo == null || !boo) {
            //不存在，直接抛出异常
            throw new LyException(ExceptionEnum.CARTS_NOT_FOUND);
        }
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);

        //判断是否有数据
        Long size = hashOps.size();
        if (size == null || size < 0) {
            //不存在，直接返回
            throw new LyException(ExceptionEnum.CARTS_NOT_FOUND);
        }

        List<String> carts = hashOps.values();
        return carts.stream()
                .map(json -> JsonUtils.toBean(json, Cart.class))
                .collect(Collectors.toList());
    }

    public void updateNum(Long skuId, Integer num) {
        //获取当前用户
        Long uid = UserHolder.getUser();
        String key = KEY_PREFIX + uid;
        //获取hash操作的对象
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
        String hashKey = skuId.toString();
        Boolean boo = hashOps.hasKey(hashKey);
        if (boo == null || !boo) {
            log.error("购物车商品不存在，用户：{}, 商品：{}", uid, skuId);
            throw new LyException(ExceptionEnum.CARTS_NOT_FOUND);
        }
        //查询购物车商品
        Cart c = JsonUtils.toBean(hashOps.get(hashKey), Cart.class);

        //修改数量
        c.setNum(num);
        //写回redis
        hashOps.put(hashKey, JsonUtils.toString(c));
    }
}
