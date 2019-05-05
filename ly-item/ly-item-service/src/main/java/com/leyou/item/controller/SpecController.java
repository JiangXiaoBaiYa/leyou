package com.leyou.item.controller;

import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/5/5 19:47
 */
@RestController
@RequestMapping("spec")
public class SpecController {

    @Autowired
    private SpecService specService;

    /**
     * 根据商品分类查询规格组
     * @param cid 商品分类id
     * @return 规格组集合
     */
    @GetMapping("groups/of/category")
    public ResponseEntity<List<SpecGroupDTO>> querySpecGroupList(@RequestParam("id")Long cid) {
        List<SpecGroupDTO> list = specService.querySpecGroupList(cid);
        return ResponseEntity.ok(list);
    }

    /**
     * 根据规格组查询规格参数
     * @param gid 规格组Id
     * @return 规格参数
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParamDTO>> querySpecParamsList(@RequestParam("gid") Long gid) {
        return ResponseEntity.ok(specService.querySpecParamsList(gid));
    }
}
