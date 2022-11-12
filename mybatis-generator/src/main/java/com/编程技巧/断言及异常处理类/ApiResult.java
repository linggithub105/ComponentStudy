package com.编程技巧.断言及异常处理类;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lijing
 * @date 2022年07月03日 16:02
 * @description 标准接口响应实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int responseCode;

    private String responseMsg;

    private boolean isSuccess;

    private T data;

    public String toString() {
        return "ApiResult(responseCode=" + this.getResponseCode() +
                ", responseMsg=" + this.getResponseMsg() +
                ", isSuccess=" + this.isSuccess() +
                ", data=" + this.getData() + ")";
    }

    public static ApiResult<String> success() {
        return success("success");
    }

    public static <T> ApiResult<T> success(T data) {
        return (new ApiResult())
                .setResponseCode(0)
                .setResponseMsg("操作成功")
                .setSuccess(true)
                .setData(data);
    }

    public static ApiResult<String> fail() {
        return fail(-1);
    }

    public static ApiResult<String> fail(int code) {
        return fail(code, "fail");
    }

    public static <T> ApiResult<T> fail(T data) {
        return fail(-1, data);
    }

    public static <T> ApiResult<T> fail(int code, T data) {
        return (new ApiResult())
                .setResponseCode(code)
                .setResponseMsg("操作失败")
                .setSuccess(false)
                .setData(data);
    }

    public static <T> ApiResult<T> success(int code, String message, T data) {
        return (new ApiResult())
                .setResponseCode(code)
                .setResponseMsg(message)
                .setSuccess(true)
                .setData(data);
    }

    public static <T> ApiResult<T> fail(int code, String message, T data) {
        return (new ApiResult())
                .setResponseCode(code)
                .setResponseMsg(message)
                .setSuccess(false)
                .setData(data);
    }
}