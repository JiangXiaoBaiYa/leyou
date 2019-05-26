package com.leyou.order.mapper;

import com.leyou.order.entity.OrderDetail;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/5/20 22:18
 */
public interface OrderDetailMapper extends Mapper<OrderDetail>, IdListMapper<OrderDetail, Long> {

    /**
     * 批量新增
     * @param details 商品详情的集合
     * @return 新增成功的行
     */
    int insertDetailList(@Param("details") List<OrderDetail> details);
}
