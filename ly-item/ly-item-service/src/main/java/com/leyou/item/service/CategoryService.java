package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.common.utils.BeanHelper;
import com.leyou.item.dto.CategoryDTO;
import com.leyou.item.entity.Category;
import com.leyou.item.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/4/29 19:59
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryMapper mapper;

    public List<CategoryDTO> queryListByParent(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> list = mapper.select(category);
        //判断结果是否查询到值了
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        //把数据转换成页面所需要的DTO形
        List<CategoryDTO> dtos = BeanHelper.copyWithCollection(list, CategoryDTO.class);
        return dtos;

    }
}
