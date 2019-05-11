package com.leyou.item.controller;

import com.leyou.item.dto.CategoryDTO;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/4/29 20:00
 */
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    /**
     * 根据父Id品牌查询
     * @param pid 父Id
     * @return 分类集合
     */
    @GetMapping("of/parent")
    public ResponseEntity<List<CategoryDTO>> queryListByParentId(@RequestParam(value = "pid",defaultValue = "0") Long pid) {
        List<CategoryDTO> dtos = service.queryListByParent(pid);
        return ResponseEntity.ok(dtos);
    }

    /**
     * 品牌修改的回显
     */
    @GetMapping("of/brand")
    public ResponseEntity<List<CategoryDTO>> queryByBrandId(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(service.queryByBrandId(id));
    }

    /**
     * 根据id的集合查询商品分类
     * @param idlist 商品分类的id集合
     * @return 分类集合
     */
    @GetMapping("list")
    public ResponseEntity<List<CategoryDTO>> queryByIds(@RequestParam("ids") List<Long> idlist) {
        return ResponseEntity.ok(service.queryByIds(idlist));
    }

    /**
     * 根据3级分类id，查询1-3级的分类
     * @param id
     * @return
     */
    @GetMapping("/levels")
    public ResponseEntity<List<CategoryDTO>> queryAllByCid3(@RequestParam("id") Long id) {
        return ResponseEntity.ok(service.queryAllByCid3(id));
    }

}
