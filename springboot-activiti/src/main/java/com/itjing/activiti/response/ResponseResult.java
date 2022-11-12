package com.itjing.activiti.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 结果返回实体类
 * @Author: lijing
 * @CreateTime: 2022-09-09 15:11
 */
@Data
@NoArgsConstructor
public class ResponseResult {

    private Integer status;

    private String message;

    private Object data;

    public ResponseResult(Integer status, String message, Object data) {
        this(status, message);
        this.data = data;
    }

    public static ResponseResult getSuccessResult(Object data) {
        ResponseResult result = new ResponseResult(200, "成功！");
        result.data = data;
        return result;

    }

    public ResponseResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}