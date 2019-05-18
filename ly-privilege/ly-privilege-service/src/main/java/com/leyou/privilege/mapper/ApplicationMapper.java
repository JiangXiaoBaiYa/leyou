package com.leyou.privilege.mapper;

import com.leyou.privilege.entity.ApplicationInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * @Author: 姜光明
 * @Date: 2019/5/18 9:49
 */
public interface ApplicationMapper extends Mapper<ApplicationInfo>, IdListMapper<Long, ApplicationInfo> {
    int insertApplicationPrivilege(@Param("serviceId") Long serviceId, @Param("idList")List<Long> idList);
}
