package com.leyou.privilege.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/5/18 9:35
 */
@Data
public class ApplicationDTO {
    private Long id;
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务密钥
     */
//    @JsonIgnore
    private String secret;
    /**
     * 服务信息
     */
    private String info;
    /**
     * 可访问的目标服务id集合
     */
    private List<Long> targetIdList;
}
