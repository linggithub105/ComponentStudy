package rocketmq.basic;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消息生产者
 */
@Component
public class SpringProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    // 发送普通消息
    public void sendMessage(String topic, String msg) {
        this.rocketMQTemplate.convertAndSend(topic, msg);
    }

    // 发送事务消息
    public void sendMessageInTransaction(String topic, String msg) throws InterruptedException {
        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 10; i++) {
            // 尝试在Header中加入一些自定义的属性。
            Message<String> message = MessageBuilder.withPayload(msg)
                    .setHeader(RocketMQHeaders.TRANSACTION_ID, "TransID_" + i)
                    // 发到事务监听器里后，这个自己设定的TAGS属性会丢失。但是上面那个属性不会丢失。
                    .setHeader(RocketMQHeaders.TAGS, tags[i % tags.length])
                    .setHeader("MyProp", "MyProp_" + i)
                    .build();
            String destination = topic + ":" + tags[i % tags.length];
            // 这里发送事务消息时，还是会转换成RocketMQ的Message对象，再调用RocketMQ的API完成事务消息机制。
            SendResult sendResult = rocketMQTemplate.sendMessageInTransaction(destination, message, destination);
            System.out.printf("%s%n", sendResult);

            Thread.sleep(10);
        }
    }
}
