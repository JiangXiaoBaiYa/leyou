package com.leyou.order.mapper;

import com.leyou.order.entity.Order;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author: 姜光明
 * @Date: 2019/5/20 22:18
 */
public interface OrderMapper extends Mapper<Order> , IdListMapper<Order,Long> {
}
