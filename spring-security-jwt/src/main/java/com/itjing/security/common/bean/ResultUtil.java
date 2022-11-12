package com.itjing.security.common.bean;

import com.itjing.security.common.enums.ResultEnum;

/**
 * @author lijing
 * @date 2022年06月09日 19:58
 * @description 结果工具类
 */
public class ResultUtil {
    /**
     * 操作成功，返回具体的数据、结果码和提示信息
     *
     * @return 结果
     */
    public static Result success() {
        final Result result = new Result(ResultEnum.OK.getCode(), ResultEnum.OK.getMsg(), null);
        return result;
    }

    public static Result success(String msg) {
        final Result result = new Result(ResultEnum.OK.getCode(), msg, null);
        return result;
    }

    /**
     * 操作失败，返回具体的数据、结果码和提示信息
     *
     * @return 结果
     */
    public static Result fail() {
        final Result result = new Result(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMsg(), null);
        return result;
    }

    public static Result fail(String msg) {
        final Result result = new Result(ResultEnum.ERROR.getCode(), msg, null);
        return result;
    }

    /**
     * 操作成功，返回具体的数据、结果码和提示信息
     *
     * @param resultEnum 枚举
     * @return 结果
     */
    public static Result success(ResultEnum resultEnum) {
        final Result result = new Result(resultEnum.getCode(), resultEnum.getMsg(), null);
        return result;
    }

}
