package com.leyou.item.dto;

import lombok.Data;

import java.util.List;


/**
 * @Author: 姜光明
 * @Date: 2019/5/5 19:51
 */
@Data
public class SpecGroupDTO {
    private Long id;

    private Long cid;

    private String name;

    private List<SpecParamDTO> params;
}