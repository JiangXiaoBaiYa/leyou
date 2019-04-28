package com.leyou.common.exceptions;

import lombok.Getter;
import org.joda.time.DateTime;

/**
 * @Author: 姜光明
 * @Date: 2019/4/27 22:03
 */
@Getter
public class ExceptionResult {
    private int status;
    private String message;
    private String timestamp;

    public ExceptionResult(LyException em) {
        this.status = em.getStatus();
        this.message = em.getMessage();
        this.timestamp = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
    }
}
