package com.itjing.minio.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lijing
 * @date 2021年03月22日 11:42
 * @description 基础返回结果类
 */
@Getter
@Setter
public class GeneralResult<T> {

    private Boolean success;
    private Integer errorCode;
    private String message;
    private T data;
    private String traceId;

    public GeneralResult(Boolean success, Integer errorCode, String message, T data) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    public static <T> GeneralResult<T> genResult(Boolean success, T data, String message) {
        return genResult(success, data, message, null);
    }

    public static <T> GeneralResult<T> genSuccessResult(T data) {
        return genResult(true, data, null, null);
    }

    public static <T> GeneralResult<T> genErrorResult(String message) {
        return genResult(false, null, message, null);
    }

    public static <T> GeneralResult<T> genSuccessResult() {
        return genResult(true, null, null, null);
    }

    public static <T> GeneralResult<T> genErrorResult(String message, Integer errorCode) {
        return genResult(false, null, message, errorCode);
    }

    public static <T> GeneralResult<T> genResult(Boolean success, T data, String message, Integer errorCode) {
        return new GeneralResult<>(success, errorCode, message, data);
    }

    public static <T> GeneralResult<T> genErrorResult(String message, Integer errorCode, String traceId) {
        GeneralResult<T> result = genResult(false, null, message, errorCode);
        result.setTraceId(traceId);
        return result;
    }
}
