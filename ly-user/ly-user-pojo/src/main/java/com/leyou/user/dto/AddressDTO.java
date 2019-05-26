package com.leyou.user.dto;

import lombok.Data;

/**
 * @Author: 姜光明
 * @Date: 2019/5/26 9:49
 */
@Data
public class AddressDTO {
    private Long id;
    private Long userId;
    private String addressee;// 收件人姓名
    private String phone;// 电话
    private String province;// 省份
    private String city;// 城市
    private String district;// 区
    private String street;// 街道地址
    private String  postcode;// 邮编
    private Boolean isDefault;
}
