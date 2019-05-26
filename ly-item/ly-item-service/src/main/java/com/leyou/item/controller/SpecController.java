package com.leyou.item.controller;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.user.dto.SpecGroupDTO;
import com.leyou.user.dto.SpecParamDTO;
import com.leyou.item.entity.SpecGroup;
import com.leyou.item.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     *
     * @param cid 商品分类id
     * @return 规格组集合
     */
    @GetMapping("groups/of/category")
    public ResponseEntity<List<SpecGroupDTO>> querySpecGroupList(@RequestParam("id") Long cid) {
        List<SpecGroupDTO> list = specService.querySpecGroupList(cid);
        return ResponseEntity.ok(list);
    }

    /**
     * 查询规格参数
     *
     * @param gid       组id
     * @param cid       分类id
     * @param searching 是否用于搜索
     * @return 规格组集合
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParamDTO>> querySpecParamsList(@RequestParam(value = "gid", required = false) Long gid,
                                                                  @RequestParam(value = "cid", required = false) Long cid,
                                                                  @RequestParam(value = "searching", required = false) Boolean searching) {
        return ResponseEntity.ok(specService.querySpecParamsList(gid, cid, searching));
    }

    /**
     * 新增规格组
     */
    @PostMapping("group")
    public ResponseEntity saveSpecGroup(@RequestBody SpecGroup specGroup) {
        try {
            specService.saveSpecGroup(specGroup);
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 删除规格组
     */
    @DeleteMapping("group/{id}")
    public ResponseEntity deleteSpecGroup(@PathVariable("id") Long id) {
        try {
            specService.deleteSpecGroup(id);
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
        return ResponseEntity.ok(200);
    }

    /**
     * 修改规格组
     */
    @PutMapping("group")
    public ResponseEntity putSpecGroup(@RequestBody SpecGroup specGroup) {
        try {
            specService.putSpecGroup(specGroup);
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
        return ResponseEntity.ok(200);
    }

    /**
     * 在规格组下新增规格参数
     */
    @PostMapping("param")
    public ResponseEntity<Void> saveSpecParams(@RequestBody SpecParamDTO specParamDTO) {
        specService.saveSpecParams(specParamDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 在规格组下修改规格参数
     */
    @PutMapping("param")
    public ResponseEntity<Void> updateSpecParams(@RequestBody SpecParamDTO specParamDTO) {
        specService.updateSpecParams(specParamDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 在规格组下删除规格参数
     */
    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> deleteSpecParams(@PathVariable("id") Long id) {
        specService.deleteSpecParams(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 查询规格参数组及组内参数
     * @param id 商品分类id
     * @return 规格组以及组内参数
     */
    @GetMapping("of/category")
    public ResponseEntity<List<SpecGroupDTO>> querySpecsByCid(@RequestParam("id") Long id) {
        return ResponseEntity.ok(specService.querySpecsByCid(id));
    }
}
