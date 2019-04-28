package com.leyou.service;

import com.leyou.pojo.Item;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @Author: 姜光明
 * @Date: 2019/4/27 20:49
 * 模拟保存商品
 */
@Service
public class ItemService {
    public Item saveItem(Item item){
        int id = new Random().nextInt(100);
        item.setId(id);
        return item;
    }
}
