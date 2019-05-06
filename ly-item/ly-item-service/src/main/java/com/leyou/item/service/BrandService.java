package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.common.utils.BeanHelper;
import com.leyou.common.vo.PageResult;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.entity.Brand;
import com.leyou.item.mapper.BrandMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/5/4 23:57
 */
@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<BrandDTO> queryBrandByPage(Integer page, Integer rows, String key, String sortBy, Boolean desc) {
        //分页
        PageHelper.startPage(page, rows);
        //过滤条件
        Example example = new Example(Brand.class);
        if (StringUtils.isNoneBlank(key)) {
            example.createCriteria().orLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());
        }
        //排序
        if (StringUtils.isNoneBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);  //id desc
        }
        //查询
        List<Brand> brands = brandMapper.selectByExample(example);

        //判断是否为空
        if (CollectionUtils.isEmpty(brands)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }

        //解析分页结果
        PageInfo<Brand> info = new PageInfo<>(brands);

        //转为BrandDTO
        List<BrandDTO> list = BeanHelper.copyWithCollection(brands, BrandDTO.class);

        return new PageResult<>(info.getTotal(), list);
    }

    /**
     * 新增品牌
     * @param cids
     */
    @Transactional
    public void saveBrand(BrandDTO brandDTO, List<Long> cids) {
        //新增品牌
        Brand brand = BeanHelper.copyProperties(brandDTO, Brand.class);
        brand.setId(null);
        int count = brandMapper.insertSelective(brand);
        if (count != 1) {
            //新增失败，抛出异常
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
        //新增成功，维护中间表
        count = brandMapper.insertCategoryBrand(brand.getId(), cids);
        if (count != cids.size()) {
            //维护中间表失败
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    @Transactional
    public void updateBrand(Brand brand, List<Long> ids) {
        //修改品牌
        int count = brandMapper.updateByPrimaryKeySelective(brand);
        if (count != 1) {
            //跟新失败，抛出异常
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }

        //删除中间表数据
        brandMapper.deleteCategoryBrand(brand.getId());

        //重新插入中间表
        count = brandMapper.insertCategoryBrand(brand.getId(), ids);
        if (count != ids.size()) {
            //维护中间表失败
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
     *  品牌删除
     * @param brand
     */
    @Transactional
    public void deleteByBrandId(Brand brand) {
        int i = brandMapper.deleteByPrimaryKey(brand);
        if (i != 1) {
            throw new LyException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }

        int count = brandMapper.deleteCategoryBrand(brand.getId());
        if (count == 0) {
            throw new LyException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }
}
