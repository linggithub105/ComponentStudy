package com.itjing.security.common.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.ToString;

/**
 * @author lijing
 * @date 2022年06月09日 19:56
 * @description 封装服务器端返回的结果
 */
@Getter
@ToString
public class Result {
    /**
     * 请求响应状态码
     */
    private int code;
    /**
     * 请求结果描述信息
     */
    private String msg;
    /**
     * 请求结果数据
     */
    private Object data;

    protected Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

    /**
     * 将key-value形式的成对出现的参数转换为JSON
     *
     * @param objs
     * @return
     */
    public Result setData(Object... objs) {
        if (objs.length % 2 != 0) {
            throw new RuntimeException("参数个数不对");
        }
        for (int i = 0; i < objs.length; i += 2) {
            if (!(objs[i] instanceof String)) {
                throw new RuntimeException("奇数参数必须为字符串");
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        // 下面两行解决Java8新日期API序列化问题
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        ObjectNode objectNode = objectMapper.createObjectNode();
        for (int i = 0; i < objs.length; i += 2) {
            objectNode.putPOJO((String) objs[i], objs[i + 1]);
        }
        this.data = objectNode;
        return this;
    }

}