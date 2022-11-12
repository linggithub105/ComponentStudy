package com.itjing.excel.handler;

import com.itjing.excel.exception.CustomException;
import com.itjing.excel.response.GeneralResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 全局异常处理类
 * @Author: lijing
 * @CreateTime: 2022-09-07 16:46
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理自定义的业务异常
    @ExceptionHandler(value = CustomException.class)
    public GeneralResult<Object> restErrorHandler(HttpServletRequest request, CustomException e) {
//        String err = "requestURI:" + request.getRequestURI()
//                + ",errorCode:" + e.getErrorCode()
//                + ",errorMsg:" + e.getMessage();
//        log.error(err, e);
        return GeneralResult.genErrorResult(
                e.getMessage(),
                e.getErrorCode(),
                e.getTraceId()
        );
    }

}
