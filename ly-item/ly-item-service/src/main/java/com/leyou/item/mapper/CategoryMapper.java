package com.leyou.item.mapper;

import com.leyou.item.entity.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/4/29 20:07
 */
public interface CategoryMapper extends Mapper<Category>/*, IdsMapper<Category>*/, IdListMapper<Category, Long> {
    @Select("select tc.id,tc.`name`,tc.parent_id,tc.is_parent,tc.sort from tb_category_brand " +
            "tcb left join tb_category tc on tcb.category_id = tc.id where tcb.brand_id = #{id}")
    List<Category> queryByBrandId(Long brandId);
}
