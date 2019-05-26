package com.leyou.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 姜光明
 * @Date: 2019/5/26 9:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long skuId;// 商品skuId
    private Integer num;// 购买数量
}
