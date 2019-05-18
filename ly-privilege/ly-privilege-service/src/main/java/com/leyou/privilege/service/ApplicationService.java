package com.leyou.privilege.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.common.utils.BeanHelper;
import com.leyou.privilege.config.PasswordConfig;
import com.leyou.privilege.dto.ApplicationDTO;
import com.leyou.privilege.entity.ApplicationInfo;
import com.leyou.privilege.mapper.ApplicationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/5/18 9:39
 */
public class ApplicationService {

    @Autowired
    private PasswordConfig passwordConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationMapper applicationMapper;

    public void save(ApplicationDTO applicationDTO) {
        //新增服务消息
        ApplicationInfo info = BeanHelper.copyProperties(applicationDTO, ApplicationInfo.class);
        info.setSecret(passwordEncoder.encode(info.getSecret()));
        int count = applicationMapper.insertSelective(info);
        if (count != 1) {
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
        //新增服务权限信息
        List<Long> idList = applicationDTO.getTargetIdList();
        count = applicationMapper.insertApplicationPrivilege(info.getId(), idList);
        if (count != idList.size()) {
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }
}
