package com.leyou.common.exceptions;

import com.leyou.common.enums.ExceptionEnum;

/**
 * @Author: 姜光明
 * @Date: 2019/4/27 21:37
 * 自定义异常，来获取枚举对象
 */
public class LyException extends RuntimeException {
    private int status;

    /**
     * 将刚才定义的枚举异常注入自定义异常中
     */
    public LyException(ExceptionEnum em) {
        super(em.getMessage());
        this.status = em.getStatus();
    }

    public LyException(ExceptionEnum em, Throwable cause) {
        super(em.getMessage(), cause);
        this.status = em.getStatus();
    }

    public LyException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
