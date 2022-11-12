package com.itjing.excel.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.MDC;

/**
 * @Description: 自定义异常
 * @Author: lijing
 * @CreateTime: 2022-09-07 14:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // 日志追踪ID
    private String traceId = MDC.get("traceId");


    public CustomException() {
    }

    /**
     * 错误编码
     */
    private Integer errorCode;

    /**
     * 消息是否为属性文件中的Key
     */
    private boolean propertiesKey = true;

    /**
     * 构造一个基本异常.
     *
     * @param message 信息描述
     */
    public CustomException(String message) {
        super(message);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode 错误编码
     * @param message   信息描述
     */
    public CustomException(Integer errorCode, String message) {
        this(errorCode, message, true);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode 错误编码
     * @param message   信息描述
     */
    public CustomException(Integer errorCode, String message, Throwable cause) {
        this(errorCode, message, cause, true);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode     错误编码
     * @param message       信息描述
     * @param propertiesKey 消息是否为属性文件中的Key
     */
    private CustomException(Integer errorCode, String message, boolean propertiesKey) {
        super(message);
        this.setErrorCode(errorCode);
        this.setPropertiesKey(propertiesKey);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode 错误编码
     * @param message   信息描述
     */
    public CustomException(Integer errorCode, String message, Throwable cause, boolean propertiesKey) {
        super(message, cause);
        this.setErrorCode(errorCode);
        this.setPropertiesKey(propertiesKey);
    }

    /**
     * 构造一个基本异常.
     *
     * @param message 信息描述
     * @param cause   根异常类（可以存入任何异常）
     */
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

}