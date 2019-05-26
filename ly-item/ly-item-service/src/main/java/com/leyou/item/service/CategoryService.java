package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.common.utils.BeanHelper;
import com.leyou.user.dto.CategoryDTO;
import com.leyou.item.entity.Category;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.mapper.SpuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/4/29 19:59
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryMapper mapper;

    @Autowired
    private SpuMapper spuMapper;

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

    public List<CategoryDTO> queryByBrandId(Long brandId) {
        List<Category> list = mapper.queryByBrandId(brandId);
        //判断结果
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(list, CategoryDTO.class);
    }

    /**
     * 根据id的集合查询商品分类
     *
     * @param idlist
     * @return
     */
    public List<CategoryDTO> queryByIds(List<Long> idlist) {
        List<Category> list = mapper.selectByIdList(idlist);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(list, CategoryDTO.class);
    }

    /**
     * 根据3级分类id，查询1-3级的分类
     *
     * @return category集合
     * @param id
     */
    public List<CategoryDTO> queryAllByCid3(Long id) {
        Category c3 = mapper.selectByPrimaryKey(id);
        if (c3 == null) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        Category c2 = mapper.selectByPrimaryKey(c3.getParentId());
        if (c2 == null) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        Category c1 = mapper.selectByPrimaryKey(c2.getParentId());
        if (c1 == null) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        List<Category> list = Arrays.asList(c1, c2, c3);
        return BeanHelper.copyWithCollection(list, CategoryDTO.class);
    }

}
