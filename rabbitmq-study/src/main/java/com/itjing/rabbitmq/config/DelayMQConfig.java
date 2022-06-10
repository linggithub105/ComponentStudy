package com.itjing.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lijing
 * @date 2022年06月02日 19:12
 * @description 延迟队列配置
 * 去官网下载延时队列插件：https://www.rabbitmq.com/community-plugins.html
 * 使用命令启用插件：
 * linux：rabbitmq-plugins enable rabbitmq_delayed_message_exchange
 * windows：进入sbin目录，执行 rabbitmq-plugins.bat enable rabbitmq_delayed_message_exchange
 */
@Configuration
public class DelayMQConfig {

    public static final String LAZY_EXCHANGE = "Ex.LazyExchange";
    public static final String LAZY_QUEUE = "MQ.LazyQueue";
    public static final String LAZY_KEY = "lazy.#";

    /**
     * 在 Exchange 的声明中可以设置exchange.setDelayed(true)来开启延迟队列，
     * 也可以设置参数内容传入交换机声明的方法中，
     * 因为第一种方式的底层就是通过这种方式来实现的。
     * @return
     */
    @Bean
    public TopicExchange lazyExchange() {
        Map<String, Object> pros = new HashMap<>();
        // 设置交换机支持延迟消息推送
//        pros.put("x-delayed-message", "topic");
        TopicExchange exchange = new TopicExchange(LAZY_EXCHANGE, true, false, pros);
        exchange.setDelayed(true);
        return exchange;
    }

    @Bean
    public Queue lazyQueue() {
        return new Queue(LAZY_QUEUE, true);
    }

    @Bean
    public Binding lazyBinding() {
        return BindingBuilder.bind(lazyQueue()).to(lazyExchange()).with(LAZY_KEY);
    }

    /**
     * 创建初始化 RabbitAdmin 对象，这样才能创建（必须）
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.setAutoStartup(true);
        rabbitAdmin.declareExchange(lazyExchange());
        rabbitAdmin.declareQueue(lazyQueue());
        rabbitAdmin.declareBinding(lazyBinding());
        return rabbitAdmin;
    }
}
