package com.itjing.entity;

import lombok.*;

/**
 * @author lijing
 * @date 2022年06月07日 14:29
 * @description 日志信息实体类
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LogInfo {

    private String id;

    private String logType;

    private String logContent;

    private String username;

    private String ipAddr;

    private String method;

    private String requestUrl;

    private String requestType;

    private Long costTime;

    private String createUser;

    private String createTime;

    private String requestParam;

}
