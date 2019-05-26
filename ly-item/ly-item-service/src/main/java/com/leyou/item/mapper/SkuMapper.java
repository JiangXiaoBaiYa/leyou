package com.leyou.item.mapper;

import com.leyou.item.entity.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @Author: 姜光明
 * @Date: 2019/5/6 20:58
 */
public interface SkuMapper extends Mapper<Sku>, InsertListMapper<Sku>, IdListMapper<Sku, Long> {
    @Update("UPDATE tb_sku SET stock = stock - #{num} WHERE id = #{id}")
    int minusStock(@Param("id") Long skuId, @Param("num") Integer num);
}
