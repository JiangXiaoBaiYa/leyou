package com.leyou.item.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.dto.CategoryDTO;
import com.leyou.item.entity.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/5/4 23:30
 */
@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<BrandDTO>> queryBrandByPage(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "sortBy",required = false)String sortBy,
            @RequestParam(value = "desc",defaultValue = "false")Boolean desc) {
        return ResponseEntity.ok(brandService.queryBrandByPage(page,rows,key,sortBy,desc));
    }

    /**
     * 品牌新增
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(/*@RequestParam("name")String name*/
                                @RequestParam("cids")List<Long> cids,
                                BrandDTO brand
                                /*@RequestParam("letter")Character letter*/) {
        brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 品牌修改
     */
    @PutMapping()
    public ResponseEntity<List<CategoryDTO>> queryByBrandId(Brand brand,@RequestParam("cids")List<Long> ids) {
        brandService.updateBrand(brand, ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * 品牌删除
     */
    @DeleteMapping()
    public ResponseEntity<Void> deleteByBrandId(Brand brand) {
        brandService.deleteByBrandId(brand);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
