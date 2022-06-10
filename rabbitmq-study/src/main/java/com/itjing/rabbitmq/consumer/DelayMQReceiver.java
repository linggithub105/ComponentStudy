package com.itjing.rabbitmq.consumer;

import com.itjing.rabbitmq.config.DelayMQConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lijing
 * @date 2022年06月02日 19:40
 * @description 延时队列消费者
 */
@Component
public class DelayMQReceiver {

    @RabbitListener(queues = DelayMQConfig.LAZY_QUEUE)
    @RabbitHandler
    public void onLazyMessage(Message msg, Channel channel) throws IOException {
        long deliveryTag = msg.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag, true);
        System.out.println("lazy receive " + new String(msg.getBody()));
    }
}
