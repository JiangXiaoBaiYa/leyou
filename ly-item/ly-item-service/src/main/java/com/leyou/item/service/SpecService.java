package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.common.utils.BeanHelper;
import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.entity.SpecGroup;
import com.leyou.item.mapper.SpecGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/5/5 19:55
 */
@Service
public class SpecService {
    @Autowired
    private SpecGroupMapper groupMapper;

    /**
     *
     * @param cid
     * @return
     */
    public List<SpecGroupDTO> querySpecGroupList(Long cid) {
        //构造查询条件
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        //开始查询
        List<SpecGroup> specGroups = groupMapper.select(specGroup);
        //健壮性判断
        if (specGroups == null) {
            throw new LyException(ExceptionEnum.SPEC_NOT_FOUND);
        }
        List<SpecGroupDTO> dtos = BeanHelper.copyWithCollection(specGroups, SpecGroupDTO.class);
        return dtos;
    }
}
