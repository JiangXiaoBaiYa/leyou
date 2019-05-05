package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.common.utils.BeanHelper;
import com.leyou.common.vo.PageResult;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.entity.Brand;
import com.leyou.item.entity.Category;
import com.leyou.item.entity.Spu;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.mapper.SpuMapper;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/5/5 21:19
 */
@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<SpuDTO> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        //构建分页
        PageHelper.startPage(page, rows);

        //构建查询条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNoneBlank(key)) {
            criteria.andLike("name", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        //添加一个默认最新时间排序
        example.setOrderByClause("update_time desc");

        //开始查询
        List<Spu> spus = spuMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(spus)) {
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        PageInfo<Spu> info = new PageInfo<>(spus);
        //把结果转成DTO
        List<SpuDTO> spuDTOS = BeanHelper.copyWithCollection(spus, SpuDTO.class);
        handleCategoryAndBrandName(spuDTOS);

        return new PageResult<>(info.getTotal(),spuDTOS);
    }

    private void handleCategoryAndBrandName(List<SpuDTO> spuDTOS) {
        for (SpuDTO spuDTO : spuDTOS) {
            List<Long> ids = spuDTO.getCategoryIds();
//           List<Category> categories2 = categoryMapper.selectByIds(ids.toString());
            List<Category> categories = categoryMapper.selectByIdList(ids);
            //开始拼接categoryName
            StrBuilder sb = new StrBuilder();
            for (Category category : categories) {
                sb.append(category.getName()).append("/");
            }
            sb.deleteCharAt(sb.length() - 1);
            String categotyName = sb.toString();
            spuDTO.setCategoryName(categotyName);


            //开始给品牌名称设置值
            Brand brand = brandMapper.selectByPrimaryKey(spuDTO.getBrandId());
            String brandName = brand.getName();
            spuDTO.setBrandName(brandName);
        }
    }

}
