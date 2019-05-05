package com.leyou.item.mapper;

import com.leyou.item.entity.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author: 姜光明
 * @Date: 2019/4/29 20:07
 */
public interface CategoryMapper extends Mapper<Category>, IdsMapper<Category>, IdListMapper<Category,Long> {
}
