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
     * 品牌查询
     * @param pid
     * @return
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

}
