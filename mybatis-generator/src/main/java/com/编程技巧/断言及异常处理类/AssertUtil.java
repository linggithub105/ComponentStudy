package com.编程技巧.断言及异常处理类;

import cn.hutool.http.HttpStatus;

/**
 * @author lijing
 * @date 2022年07月03日 16:09
 * @description 断言类
 */
public class AssertUtil {

    public AssertUtil() {
    }


    /**
     * 抛出异常(默认错误1000)
     *
     * @param message
     */
    public static void businessInvalid(String message) {
        throw new BusinessException(HttpStatus.HTTP_NOT_ACCEPTABLE, message);
    }


    /**
     * 表达式为真即抛出异常
     *
     * @param expression
     * @param message
     */
    public static void businessInvalid(boolean expression, int code, String message) {
        if (expression) {
            throw new BusinessException(code, message);
        }
    }
}