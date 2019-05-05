package com.leyou.item.mapper;

import com.leyou.item.entity.Brand;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/5/4 23:29
 */
public interface BrandMapper extends Mapper<Brand> {
    /**
     *  新增商品分类和品牌中间表数据
     * @param bid 品牌id
     * @param cids 商品分类id集合
     * @return 新增条数
     */
    int insertCategoryBrand(@Param("bid") Long bid,@Param("ids") List<Long> cids);
}
