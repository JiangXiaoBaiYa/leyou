package com.leyou.user.dto;

import lombok.Data;

/**
 * @Author: 姜光明
 * @Date: 2019/5/5 20:16
 */
@Data
public class SpecParamDTO {
    private Long id;
    private Long cid;
    private Long groupId;
    private String name;
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;
}