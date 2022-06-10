package com.itjing.rabbitmq.provider;

import com.itjing.rabbitmq.config.DelayMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author lijing
 * @date 2022年06月02日 19:31
 * @description 延迟队列消息发送者
 */
@Component
@Slf4j
public class DelayMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendLazy(Object message, Integer delayTime) {

        rabbitTemplate.setMandatory(true);

//        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            log.info("CorrelationData content : " + correlationData);
            log.info("Ack status : " + ack);
            log.info("Cause content : " + cause);
            if (ack) {
                log.info("消息成功发送");
            } else {
                log.info("消息发送失败：" + correlationData + ", 出现异常：" + cause);
            }
        }));

//        rabbitTemplate.setReturnCallback(returnCallback);
        rabbitTemplate.setReturnCallback((msg, replyCode, replyText, exchange, routingKey) -> {
            log.info("被退回的消息为：{}", msg);
            log.info("replyCode：{}", replyCode);
            log.info("replyText：{}", replyText);
            log.info("exchange：{}", exchange);
            log.info("routingKey：{}", routingKey);
        });

        // id + 时间戳 全局唯一
        CorrelationData correlationData = new CorrelationData("12345678909" + new Date());

        // 发送消息时指定 header 延迟时间
        rabbitTemplate.convertAndSend(DelayMQConfig.LAZY_EXCHANGE, "lazy.boot", message,
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        // 设置消息持久化
                        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        // message.getMessageProperties().setHeader("x-delay", "6000");
                        // 可以观察 setDelay(Integer i) 底层代码
                        // 也是在 header 中设置 x-delay，等同于手动设置 header
                        message.getMessageProperties().setDelay(delayTime);
                        return message;
                    }
                }, correlationData);
    }

}
