package com.leyou.order.mapper;

import com.leyou.order.entity.OrderLogistics;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author: 姜光明
 * @Date: 2019/5/20 22:19
 */
public interface OrderLogisticsMapper extends Mapper<OrderLogistics>, IdListMapper<OrderLogistics,Long> {
}
