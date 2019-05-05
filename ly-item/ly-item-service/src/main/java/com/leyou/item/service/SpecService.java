package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.common.utils.BeanHelper;
import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.entity.SpecGroup;
import com.leyou.item.entity.SpecParam;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/5/5 19:55
 */
@Service
public class SpecService {
    @Autowired
    private SpecGroupMapper groupMapper;

    @Autowired
    private SpecParamsMapper paramsMapper;

    /**
     *根据商品分类查询规格组
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

    /**
     * 根据规格组查询规格参数
     * @param gid
     * @return
     */
    public List<SpecParamDTO> querySpecParamsList(Long gid) {
        //构造查询条件
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        //开始查询
        List<SpecParam> specParams = paramsMapper.select(specParam);

        //健壮性判断
        if (CollectionUtils.isEmpty(specParams)) {
            throw new LyException(ExceptionEnum.SPEC_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(specParams, SpecParamDTO.class);
    }

    /**
     * 新增规格组
     * @param specGroup
     */
    @Transactional
    public void saveSpecGroup(SpecGroup specGroup) {
        groupMapper.insertSelective(specGroup);
    }

    /**
     * 删除规格组
     * @param id
     */
    @Transactional
    public void deleteSpecGroup(Long id) {
        groupMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改规格组
     *
     * @param specGroup
     */
    public void putSpecGroup(SpecGroup specGroup) {
        groupMapper.updateByPrimaryKeySelective(specGroup);
    }
}
