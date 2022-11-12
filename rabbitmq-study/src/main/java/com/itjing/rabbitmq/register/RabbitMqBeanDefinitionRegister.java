package com.itjing.rabbitmq.register;

import com.itjing.rabbitmq.enums.DelayQueueEnum;
import com.itjing.rabbitmq.enums.ExchangeEnum;
import com.itjing.rabbitmq.enums.QueueEnum;
import com.itjing.rabbitmq.service.impl.RabbitMqServiceImpl;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author lijing
 * @date 2022年06月20日 10:13
 * @description 开启mq后，注入对应的bean，使用时自动注入即可
 */
public class RabbitMqBeanDefinitionRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private String host;
    private Integer port;
    private String virtualHost;
    private String username;
    private String password;

    @Override
    public void setEnvironment(Environment environment) {
        try {
            this.host = environment.getProperty("spring.rabbitmq.host");
            this.port = Integer.valueOf(environment.getProperty("spring.rabbitmq.port"));
            this.virtualHost = environment.getProperty("spring.rabbitmq.virtual-host");
            this.username = environment.getProperty("spring.rabbitmq.username");
            this.password = environment.getProperty("spring.rabbitmq.password");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        // 注入CachingConnectionFactory
        BeanDefinition connectionFactoryBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(CachingConnectionFactory.class, () -> {
            CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
            cachingConnectionFactory.setHost(host);
            cachingConnectionFactory.setPort(port);
            cachingConnectionFactory.setUsername(username);
            cachingConnectionFactory.setPassword(password);
            cachingConnectionFactory.setVirtualHost(virtualHost);
            return cachingConnectionFactory;
        }).getBeanDefinition();
        registry.registerBeanDefinition(CachingConnectionFactory.class.getName(), connectionFactoryBeanDefinition);

        // 注入RabbitAdmin
        BeanDefinition rabbitAdminBeanDefinition = BeanDefinitionBuilder.rootBeanDefinition(RabbitAdmin.class)
                .addConstructorArgReference(CachingConnectionFactory.class.getName())
                .addPropertyValue("autoStartup", true)
                .getBeanDefinition();
        registry.registerBeanDefinition(RabbitAdmin.class.getName(), rabbitAdminBeanDefinition);

        // 注入RabbitTemplate
        BeanDefinition rabbitTemplateBeanDefinition = BeanDefinitionBuilder.rootBeanDefinition(RabbitTemplate.class)
                .addConstructorArgReference(CachingConnectionFactory.class.getName())
                .getBeanDefinition();
        registry.registerBeanDefinition(RabbitTemplate.class.getName(), rabbitTemplateBeanDefinition);

        // 注入RabbitMqServiceImpl
        BeanDefinition rabbitMqServiceImplBeanDefinition = BeanDefinitionBuilder.rootBeanDefinition(RabbitMqServiceImpl.class)
                .addConstructorArgReference(RabbitTemplate.class.getName())
                .addConstructorArgReference(RabbitAdmin.class.getName())
                .getBeanDefinition();
        registry.registerBeanDefinition(RabbitMqServiceImpl.class.getName(), rabbitMqServiceImplBeanDefinition);

        // 注入交换机
        for (ExchangeEnum value : ExchangeEnum.values()) {
            switch (value.getType()) {
                case topic:
                    BeanDefinition topicExchange = BeanDefinitionBuilder.genericBeanDefinition(TopicExchange.class, () -> new TopicExchange(value.getName(), value.getDurable(), false)).getBeanDefinition();
                    registry.registerBeanDefinition(value.getName(), topicExchange);
                    break;
                case direct:
                    BeanDefinition directExchange = BeanDefinitionBuilder.genericBeanDefinition(DirectExchange.class, () -> new DirectExchange(value.getName(), value.getDurable(), false)).getBeanDefinition();
                    registry.registerBeanDefinition(value.getName(), directExchange);
                    break;
                case fanout:
                    BeanDefinition fanoutExchange = BeanDefinitionBuilder.genericBeanDefinition(FanoutExchange.class, () -> new FanoutExchange(value.getName(), value.getDurable(), false)).getBeanDefinition();
                    registry.registerBeanDefinition(value.getName(), fanoutExchange);
                    break;
                default:
                    break;
            }
        }

        for (QueueEnum value : QueueEnum.values()) {
            BeanDefinition queue = BeanDefinitionBuilder.genericBeanDefinition(Queue.class, () -> new Queue(value.getName())).getBeanDefinition();
            registry.registerBeanDefinition(value.getName(), queue);
        }

        for (DelayQueueEnum value : DelayQueueEnum.values()) {
            Map<String, Object> params = DelayQueueEnum.params(value.getDeadExchangeEnum().getName(), value.getDeadQueueEnum().getName(), value.getTtl());
            BeanDefinition queue = BeanDefinitionBuilder.genericBeanDefinition(Queue.class, () -> QueueBuilder.durable(value.getName()).withArguments(params).build()).getBeanDefinition();
            registry.registerBeanDefinition(value.getName(), queue);
        }
    }

}
