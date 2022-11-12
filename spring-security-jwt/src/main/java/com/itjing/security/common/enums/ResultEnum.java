package com.itjing.security.common.enums;

/**
 * @author lijing
 * @date 2022年06月09日 19:58
 * @description 结果信息枚举类
 */
public enum ResultEnum {

    /**
     * 成功
     */
    OK(200, "Success"),
    /**
     * 成功
     */
    SUCCESS(200, "成功"),
    /**
     * 找不到
     */
    NOT_FOUND(404, "Not Found"),

    /**
     * 服务器错误
     */
    ERROR(500, "Server Error"),

    /**
     * 参数不正确
     */
    PARAM_ERROR(501, "Parameter Error"),

    USER_NEED_AUTHORITIES(201, "用户未登录"),
    USER_LOGIN_FAILED(202, "用户账号或密码错误"),
    USER_LOGIN_SUCCESS(203, "用户登录成功"),
    USER_NO_ACCESS(204, "用户无权访问"),
    USER_LOGOUT_SUCCESS(205, "用户登出成功"),
    TOKEN_IS_BLACKLIST(206, "此token为黑名单"),
    LOGIN_IS_OVERDUE(207, "登录已失效"),

    ;

    /**
     * 操作代码
     */
    int code;

    /**
     * 提示信息
     */
    String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
