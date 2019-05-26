package com.leyou.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: 姜光明
 * @Date: 2019/5/26 9:42
 */
@Data
@ConfigurationProperties(prefix = "ly.worker")
public class IdWorkerProperties {
    private long workerId;// 当前机器id

    private long dataCenterId;// 序列号
}
