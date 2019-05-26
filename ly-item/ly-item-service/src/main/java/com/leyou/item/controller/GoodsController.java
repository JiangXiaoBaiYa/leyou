package com.leyou.item.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.user.dto.SkuDTO;
import com.leyou.user.dto.SpuDTO;
import com.leyou.user.dto.SpuDetailDTO;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @Author: 姜光明
 * @Date: 2019/5/5 21:14
 * 把与商品相关的一切业务接口都放到一起
 */
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 商品的分页查询
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuDTO>> querySpuByPage(
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "saleable", required = false)Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "5")Integer rows
    ) {
        return ResponseEntity.ok(goodsService.querySpuByPage(page, rows, saleable, key));
    }

    /**
     * 商品信息的保存
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuDTO spuDTO) {
        goodsService.saveGoods(spuDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改商品的上/下架
     */
    @PutMapping("spu/saleable")
    public ResponseEntity<Void> updateSaleable(@RequestParam("id") Long id,
                                               @RequestParam("saleable") Boolean saleable) {
        goodsService.updateSaleable(id, saleable);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 商品的数据回显修改之根据spuid查找supDetail
     */
    @GetMapping("spu/detail")
    public ResponseEntity<SpuDetailDTO> querySpuDetailById(@RequestParam("id")Long id) {
        return ResponseEntity.ok(goodsService.querySpuDetailById(id));
    }

    /**
     * 商品的数据回显之根据spuid查找sku
     */
    @GetMapping("sku/of/spu")
    public ResponseEntity<List<SkuDTO>> querySkuById(@RequestParam("id")Long id) {
        return ResponseEntity.ok(goodsService.querySkuById(id));
    }

    /**
     * 商品信息回显后的修改
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuDTO spuDTO) {
        goodsService.updateGoods(spuDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 商品的删除（删spu，sku，spudetail表）
     */
    @DeleteMapping("spu/delete")
    public ResponseEntity<Void> deleteGoodsBySpuid(@RequestParam("id") Long id) {
        goodsService.deleteGoodsBySpuid(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    public ResponseEntity<SpuDTO> querySpuBySpuid(@PathVariable("id") Long id) {
        return ResponseEntity.ok(goodsService.querySpuBySpuid(id));
    }

    /**
     * 根据ids批量查询sku
     * @param ids sku id的集合
     * @return sku集合
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<SkuDTO>> querySkuByIds(@RequestParam("ids") List<Long> ids) {

        return ResponseEntity.ok(goodsService.querySkuByIds(ids));
    }

    /**
     * 减库存
     * @param cartMap 商品id及数量的map
     */
    @PutMapping("/stock/minus")
    public ResponseEntity<Void> minusStock(@RequestBody Map<Long, Integer> cartMap){
        goodsService.minusStock(cartMap);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
