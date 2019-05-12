package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.common.utils.BeanHelper;
import com.leyou.common.vo.PageResult;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.dto.SpuDetailDTO;
import com.leyou.item.entity.*;
import com.leyou.item.mapper.*;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/5/5 21:19
 */
@Service
@SuppressWarnings("all")
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SpuDetailMapper detailMapper;

    /**
     * 商品的分页查询
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
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

    /**
     * 抽取出来的一个方法
     * @param spuDTOS
     */
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

    /**
     * 商品新增
     * @param spuDTO
     */
    @Transactional
    public void saveGoods(SpuDTO spuDTO) {
        //新增spu表
        Spu spu = BeanHelper.copyProperties(spuDTO, Spu.class);
        spu.setId(null);
        spu.setCreateTime(null);
        spu.setSaleable(null);
        int count = spuMapper.insertSelective(spu);
        if (count != 1) {
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

        //新增spuDetail表
        SpuDetail spuDetail = BeanHelper.copyProperties(spuDTO.getSpuDetail(), SpuDetail.class);
        spuDetail.setSpuId(spu.getId());
       count = detailMapper.insertSelective(spuDetail);
        if (count != 1) {
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

        //新增sku表
        List<Sku> skus = BeanHelper.copyWithCollection(spuDTO.getSkus(), Sku.class);
        for (Sku sku : skus) {
            sku.setSpuId(spu.getId());
            skuMapper.insertSelective(sku);
        }
    }

    /**
     * 修改商品的上/下架
     *
     * @param id
     * @param saleable
     */
    @Transactional
    public void updateSaleable(Long id, Boolean saleable) {
        //先修改spu表中的saleable字段
        Spu spu = new Spu();
        spu.setId(id);
        spu.setSaleable(saleable);
        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if (count != 1) {
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
        //再修改sku表中的enable字段
        Sku sku = new Sku();
        sku.setEnable(saleable);
        //构建查询条件
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", id);
        //参数一是怎么样修改，参数二是查询条件
        skuMapper.updateByExampleSelective(sku, example);

    }

    /**
     * 商品修改的数据回显
     *(暂时没用到此方法)
     * @param id
     * @return
     */
    public SpuDTO querySpuById(Long id) {
        //查询spu表并把数据封装到spu中
        Spu spu = new Spu();
        spu.setId(id);
        SpuDTO spuDTO = BeanHelper.copyProperties(spuMapper.selectOne(spu), SpuDTO.class);

        //查询detail表并封装
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(id);
        spuDTO.setSpuDetail(BeanHelper.copyProperties(detailMapper.selectOne(spuDetail), SpuDetailDTO.class));

        //查询sku表并封装
        Sku sku = new Sku();
        sku.setSpuId(id);
        spuDTO.setSkus(BeanHelper.copyWithCollection(skuMapper.select(sku), SkuDTO.class));

        return spuDTO;
    }

    /**
     * 商品的数据回显之根据spuid查找sku
     *
     * @param id
     * @return
     */
    public List<SkuDTO> querySkuById(Long id) {
        //查询sku表并封装
        Sku sku = new Sku();
        sku.setSpuId(id);
        return BeanHelper.copyWithCollection(skuMapper.select(sku), SkuDTO.class);
    }

    /**
     * 商品的数据回显修改之根据spuid查找supDetail
     *
     * @param id
     * @return
     */
    public SpuDetailDTO querySpuDetailById(Long id) {
        //查询detail表
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(id);
        return BeanHelper.copyProperties(detailMapper.selectOne(spuDetail), SpuDetailDTO.class);
    }


    /**
     * 商品信息回显后的修改
     */
    @Transactional
    public void updateGoods(SpuDTO spuDTO) {
        //修改spu表
        Spu spu = BeanHelper.copyProperties(spuDTO, Spu.class);
        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if (count != 1) {
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
        //修改spuDetail表
        SpuDetail spuDetail = BeanHelper.copyProperties(spuDTO.getSpuDetail(), SpuDetail.class);
        count = detailMapper.updateByPrimaryKeySelective(spuDetail);
        if (count != 1) {
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
        //删除sku表
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", spuDTO.getId());
        count = skuMapper.deleteByExample(example);
        if (count == 0) {
            throw new LyException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
        //新增sku表
        List<Sku> skus = BeanHelper.copyWithCollection(spuDTO.getSkus(), Sku.class);
        for (Sku sku : skus) {
            sku.setSpuId(spuDTO.getId());
            skuMapper.insertSelective(sku);
        }
    }

    /**
     * 商品的删除（删spu，sku，spudetail表）
     *
     * @param id spuId
     */
    @Transactional
    public void deleteGoodsBySpuid(Long id) {
        //删除spu表
        Spu spu = new Spu();
        spu.setId(id);
        int count = spuMapper.delete(spu);
        if (count != 1) {
            throw new LyException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
        //删除spuDetail表
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(id);
        count = detailMapper.deleteByPrimaryKey(spuDetail);
        if (count != 1) {
            throw new LyException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
        //删除sku表
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", id);
        count = skuMapper.deleteByExample(example);
        if (count == 0) {
            throw new LyException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }

    /**
     * 根据spu的id查询spu
     *
     * @param id
     * @return
     */
    public SpuDTO querySpuBySpuid(Long id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        SpuDTO spuDTO = BeanHelper.copyProperties(spu, SpuDTO.class);

        //查询spuDetail
        spuDTO.setSpuDetail(querySpuDetailById(id));

        //查询skus
        spuDTO.setSkus(querySkuById(id));
        return spuDTO;
    }
}
