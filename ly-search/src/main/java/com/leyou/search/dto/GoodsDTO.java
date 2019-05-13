package com.leyou.search.dto;

import lombok.Data;

/**
 * @Author: 姜光明
 * @Date: 2019/5/8 20:19
 * 要在页面上展示的数据
 */
@Data
public class GoodsDTO {
    private Long id; // spuId
    private String subTitle;// 卖点
    private String skus;// sku信息的json结构
}
