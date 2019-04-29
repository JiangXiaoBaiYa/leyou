package com.leyou.item.dto;

import lombok.Data;

/**
 * @Author: 姜光明
 * @Date: 2019/4/29 20:10
 */
@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;
}
