package com.leyou.common.enums;

import lombok.Getter;

/**
 * @Author: 姜光明
 * @Date: 2019/4/27 21:43
 */
@Getter
public enum ExceptionEnum {
    PRICE_CANNOT_BE_NULL(600,"价格不能为空！!!!!!!!!");
    private int status;
    private String message;

    ExceptionEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
