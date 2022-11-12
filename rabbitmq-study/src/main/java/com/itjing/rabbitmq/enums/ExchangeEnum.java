package com.itjing.rabbitmq.enums;

/**
 * @author lijing
 * @date 2022年06月20日 10:15
 * @description 交换机枚举
 */
public enum ExchangeEnum {

    DEFAULT_EXCHANGE("default_exchange", ExchangeType.direct, true, "默认交换机"),

    DEFAULT_DELAY_EXCHANGE("default_delay_exchange", ExchangeType.direct, true, "默认延迟交换机"),

    DEFAULT_DEAD_EXCHANGE("default_dead_exchange", ExchangeType.direct, true, "默认死信交换机");

    // 交换机名称
    private String name;
    // 交换机类型
    private ExchangeType type;
    // true持久化 交换机 消息在服务重启后存在
    private Boolean durable;
    // 长时间不使用交换机系统自动删除
//        private Boolean autoDelete;
    // 描述
    private String desc;

    ExchangeEnum(String name, ExchangeType type, Boolean durable, String desc) {
        this.name = name;
        this.type = type;
        this.durable = durable;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public ExchangeType getType() {
        return type;
    }

    public Boolean getDurable() {
        return durable;
    }

    public String getDesc() {
        return desc;
    }
}
