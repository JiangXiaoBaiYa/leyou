package com.leyou.web;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.pojo.Item;
import com.leyou.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 姜光明
 * @Date: 2019/4/27 20:52
 */
@RestController
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping("item")
    public ResponseEntity<Item> saveItem(Item item) {
        //如果价格为空，则抛出异常,返回400状态码，请求参数有误
        if (item.getPrice() == null) {
            throw new LyException(ExceptionEnum.PRICE_CANNOT_BE_NULL);
        }
        Item item1 = itemService.saveItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);

    }

//    @PostMapping("item")
//    public ResponseEntity<Item> saveItem(Item item) {
//        //如果价格为空，则抛出异常,返回400状态码，请求参数有误
//        if (item.getPrice() == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
////            throw new RuntimeException("price不能为空");
//        }
//        Item item1 = itemService.saveItem(item);
//        return ResponseEntity.status(HttpStatus.CREATED).body(item);
//    }
}
