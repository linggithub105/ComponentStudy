package com.itjing.response;

import com.itjing.error.CommonResultCode;
import com.itjing.error.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description: 统一返回结果类
 * @Author: lijing
 * @CreateTime: 2022-09-30 09:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BaseResponse<T> {


    private Integer code;

    private String desc;

    private String message;

    private T data;

    private Map<String, Object> extra;


    public Boolean isSuccess() {
        return CommonResultCode.SUCCESS.getCode().equals(code);
    }

    public static <T> BaseResponse<T> success(T data) {
        return code().data(data).build();
    }

    public static <T> BaseResponse<T> error(T data) {
        return code(CommonResultCode.SERVER_ERROR).data(data).build();
    }


    public static BaseResponseBuilder code() {
        return code(CommonResultCode.SUCCESS);
    }

    public static BaseResponseBuilder code(ResultCode resultCode) {
        return new BaseResponseBuilder(resultCode);
    }

    public static class BaseResponseBuilder<T> {

        private ResultCode resultCode;

        private T data;

        private Map<String, Object> extra;

        public BaseResponseBuilder(ResultCode resultCode) {
            this.resultCode = resultCode;
        }

        public BaseResponseBuilder data(T data) {
            this.data = data;
            return this;
        }


        public BaseResponseBuilder extra(Map<String, Object> extra) {
            this.extra = extra;
            return this;
        }

        public BaseResponse<T> build() {
            return new BaseResponse(resultCode.getCode(), resultCode.getDesc(), resultCode.getMsg(), data, extra);
        }
    }

}