package com.leyou.common.advice;

import com.leyou.common.exceptions.ExceptionResult;
import com.leyou.common.exceptions.LyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Author: 姜光明
 * @Date: 2019/4/27 21:08
 */
@ControllerAdvice
@Slf4j
public class BasicExceptionAdvice {
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<String> handleException(RuntimeException em) {
//        log.error(em.getMessage(),em);
//        // 我们暂定返回状态码为400， 然后从异常中获取友好提示信息
//        return ResponseEntity.status(400).body(em.getMessage());
//    }

    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> handleException(LyException em) {
        log.error(em.getMessage(),em);
        return ResponseEntity.status(em.getStatus()).body(new ExceptionResult(em));
    }
}
