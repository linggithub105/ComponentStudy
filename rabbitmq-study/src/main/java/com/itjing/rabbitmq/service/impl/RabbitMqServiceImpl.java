package com.itjing.rabbitmq.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.itjing.rabbitmq.enums.DelayQueueEnum;
import com.itjing.rabbitmq.enums.ExchangeEnum;
import com.itjing.rabbitmq.enums.QueueEnum;
import com.itjing.rabbitmq.service.MqService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author lijing
 * @date 2022年06月20日 10:26
 * @description rabbitmq 公共调用方法
 */
public class RabbitMqServiceImpl implements MqService {

    private RabbitTemplate rabbitTemplate;

    private RabbitAdmin rabbitAdmin;

    public RabbitMqServiceImpl(RabbitTemplate rabbitTemplate, RabbitAdmin rabbitAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitAdmin = rabbitAdmin;
    }

    @Override
    public void send(Object msg, QueueEnum queueEnum) throws Exception {
        ExchangeEnum exchangeEnum = ExchangeEnum.DEFAULT_EXCHANGE;
        this.send(msg, exchangeEnum, queueEnum);
    }

    @Override
    public void send(Object msg, ExchangeEnum exchangeEnum, QueueEnum queueEnum) throws Exception {
        this.bindExchangeAndQueue(exchangeEnum, queueEnum);
        Message message = MessageBuilder.withBody(new ObjectMapper().writeValueAsBytes(msg)).build();
        rabbitTemplate.convertAndSend(exchangeEnum.getName(), queueEnum.getName(), message);
    }

    @Override
    public void sendDelay(Object msg, DelayQueueEnum delayQueueEnum) throws Exception {
        ExchangeEnum exchangeEnum = ExchangeEnum.DEFAULT_EXCHANGE;
        this.sendDelay(msg, exchangeEnum, delayQueueEnum);
    }

    @Override
    public void sendDelay(Object msg, ExchangeEnum delayExchangeEnum, DelayQueueEnum delayQueueEnum) throws Exception {
        // 绑定死信
        this.bindExchangeAndQueue(delayQueueEnum.getDeadExchangeEnum(), delayQueueEnum.getDeadQueueEnum());
        // 绑定延迟
        this.bindDelayExchangeAndQueue(delayExchangeEnum, delayQueueEnum);

        Message message = MessageBuilder.withBody(new ObjectMapper().writeValueAsBytes(msg)).build();
        rabbitTemplate.convertAndSend(delayExchangeEnum.getName(), delayQueueEnum.getName(), message);

    }

    /**
     * 创建交换机
     *
     * @param exchangeEnum
     * @throws Exception
     */
    private void createMyExchange(ExchangeEnum exchangeEnum) throws Exception {
        switch (exchangeEnum.getType()) {
            case direct:
                rabbitAdmin.declareExchange(new DirectExchange(exchangeEnum.getName(), exchangeEnum.getDurable(), false));
                break;
            case topic:
                rabbitAdmin.declareExchange(new TopicExchange(exchangeEnum.getName(), exchangeEnum.getDurable(), false));
                break;
            case fanout:
                rabbitAdmin.declareExchange(new FanoutExchange(exchangeEnum.getName(), exchangeEnum.getDurable(), false));
                break;
            default:
                throw new Exception("请指定交换机类型");
        }

    }

    /**
     * 创建队列
     *
     * @param queueEnum
     * @throws Exception
     */
    private void createMyQueue(QueueEnum queueEnum) throws Exception {
        if (null != queueEnum.getArguments() && queueEnum.getArguments().size() > 0) {
            rabbitAdmin.declareQueue(new Queue(queueEnum.getName(), queueEnum.getDurable(), false, false, queueEnum.getArguments()));
            return;
        }
        rabbitAdmin.declareQueue(new Queue(queueEnum.getName(), queueEnum.getDurable()));
    }

    /**
     * 创建队列
     *
     * @param delayQueueEnum
     * @throws Exception
     */
    private void createMyDelayQueue(DelayQueueEnum delayQueueEnum) throws Exception {
        Map<String, Object> params = DelayQueueEnum.params(delayQueueEnum.getDeadExchangeEnum().getName(), delayQueueEnum.getDeadQueueEnum().getName(), delayQueueEnum.getTtl());
        Queue queue = QueueBuilder.durable(delayQueueEnum.getName()).withArguments(params).build();
        rabbitAdmin.declareQueue(queue);
    }

    /**
     * 绑定 队列与交换机
     *
     * @param exchangeEnum
     * @param queueEnum
     */
    private void bindExchangeAndQueue(ExchangeEnum exchangeEnum, QueueEnum queueEnum) {

        rabbitAdmin.declareBinding(new Binding(
                queueEnum.getName(),
                Binding.DestinationType.QUEUE,
                exchangeEnum.getName(),
                queueEnum.getName(),
                null
        ));
    }


    /**
     * 绑定延时 队列 与交换机
     *
     * @param exchangeEnum
     * @param delayQueueEnum
     */
    private void bindDelayExchangeAndQueue(ExchangeEnum exchangeEnum, DelayQueueEnum delayQueueEnum) {

        rabbitAdmin.declareBinding(new Binding(
                delayQueueEnum.getName(),
                Binding.DestinationType.QUEUE,
                exchangeEnum.getName(),
                delayQueueEnum.getName(),
                null
        ));
    }

    /**
     * 发送消息LocalDateTime时间格式化问题
     * 这里ObjectMapper需要自定义序列化配置
     */
    public void testObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper ();
        // 为jackjson注册序列化提供能力的对象
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // 系列化时间格式化
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        // 反序列化时间格式化
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        // 注册进入jackson
        objectMapper.registerModule(javaTimeModule);
    }
}

