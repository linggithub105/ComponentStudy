package com.itjing.sensitive.handler;

/**
 * @Description:
 * @Author: lijing
 * @CreateTime: 2022-11-03 11:05
 */
public interface SensitiveOperation {
    String mask(String content, String maskChar);

}
