package com.itjing.rabbitmq.enums;

import lombok.Getter;

import java.util.Map;

/**
 * @author lijing
 * @date 2022年06月20日 10:18
 * @description 队列枚举
 */
@Getter
public enum QueueEnum {

    DEFAULT_QUEUE("default_queue", true, null, "默认队列"),

    DEFAULT_DEAD_QUEUE("default_dead_queue", true, null, "默认死信队列"),

    SERVICE_NOTIFY_DEAD_QUEUE("service_notify_dead_queue", true, null, "业务回调死信队列");

    // 队列名称
    private String name;
    // 持久化
    private Boolean durable;
    // 参数
    private Map<String, Object> arguments;
    // 队列描述
    private String desc;

    QueueEnum(String name, Boolean durable, Map<String, Object> arguments, String desc) {
        this.name = name;
        this.durable = durable;
        this.arguments = arguments;
        this.desc = desc;
    }

}
