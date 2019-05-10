package com.leyou.item;

import com.leyou.common.vo.PageResult;
import com.leyou.item.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/5/8 20:27
 */
@FeignClient("item-service")
public interface ItemClient {
    /**
     * 根据id的集合查询商品分类
     * @param idlist 商品分类的id集合
     * @return 分类集合
     */
    @GetMapping("category/list")
    List<CategoryDTO> queryByIds(@RequestParam("ids") List<Long> idlist);

    /**
     * 根据品牌id进行品牌的查询
     * @param id 品牌id
     * @return 品牌
     */
    @GetMapping("brand/{id}")
    BrandDTO queryBrandByBrandId(@PathVariable("id") Long id);

    /**
     * 商品的分页查询
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    PageResult<SpuDTO> querySpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows);

    /**
     * 根据spuid查找supDetail
     */
    @GetMapping("spu/detail")
    SpuDetailDTO querySpuDetailById(@RequestParam("id") Long id);

    /**
     * 商品的数据回显之根据spuid查找sku
     */
    @GetMapping("sku/of/spu")
    List<SkuDTO> querySkuById(@RequestParam("id") Long id);
    /**
     * 查询规格参数
     * @param gid 组id
     * @param cid 分类id
     * @param searching 是否用于搜索
     * @return 规格组集合
     */
    @GetMapping("spec/params")
    List<SpecParamDTO> querySpecParamsList(@RequestParam(value = "gid", required = false) Long gid,
                                                                  @RequestParam(value = "cid", required = false) Long cid,
                                           @RequestParam(value = "searching", required = false) Boolean searching);

    /**
     * 根据品牌id批量查询品牌
     *
     * @param idlist 品牌id的集合
     * @return 品牌的集合
     */
    @GetMapping("brand/list")
    List<BrandDTO> queryBrandByIds(@RequestParam("ids") List<Long> idlist);
}
