package com.编程技巧.断言及异常处理类;

import java.util.logging.Level;

/**
 * @author lijing
 * @date 2022年07月03日 16:08
 * @description 业务异常，异常信息会返回到前端展示给用户
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1;

    private int code = 1;

    private Level level;

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Level level, String message) {
        super(message);
        this.level = level;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public final Level getLevel() {
        return this.level;
    }
}