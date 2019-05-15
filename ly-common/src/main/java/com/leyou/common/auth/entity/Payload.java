package com.leyou.common.auth.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author: 姜光明
 * @Date: 2019/5/15 20:18
 * JWT中，保存载荷数据的对象，暂时计划存储三部分
 */
@Data
public class Payload<T> {
    private String id;  //jwt的id
    private T userInfo;  //用户数据，不确定，可以是任意类型
    private Date expiration;  //Date
}