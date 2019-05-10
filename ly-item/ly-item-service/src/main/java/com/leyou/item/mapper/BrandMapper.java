package com.leyou.item.mapper;

import com.leyou.item.entity.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/5/4 23:29
 */
public interface BrandMapper extends Mapper<Brand>, IdListMapper<Brand,Long> {
    /**
     *  新增商品分类和品牌中间表数据
     * @param bid 品牌id
     * @param cids 商品分类id集合
     * @return 新增条数
     */
    int insertCategoryBrand(@Param("bid") Long bid,@Param("ids") List<Long> cids);

    @Delete("delete from tb_category_brand where brand_id = #{bid}")
    int deleteCategoryBrand(@Param("bid") Long bid);

    @Select("select b.id,b.name,b.image,b.letter from tb_category_brand cb inner join tb_brand b on cb.brand_id = b.id where category_id = #{cid}")
    List<Brand> queryBrandByCategoryId(@Param("cid") Long cid);
}
