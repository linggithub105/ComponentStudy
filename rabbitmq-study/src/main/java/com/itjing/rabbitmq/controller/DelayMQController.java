package com.itjing.rabbitmq.controller;

import com.itjing.rabbitmq.provider.DelayMQSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijing
 * @date 2022年06月02日 19:46
 * @description 延迟队列控制器
 */
@RestController
@Slf4j
@RequestMapping("/delay")
public class DelayMQController {

    @Autowired
    private DelayMQSender mqSender;

    /**
     * 6 秒后收到了消息
     */
    @GetMapping("/send")
    public void sendLazy() {
        String msg = "hello spring boot";
        mqSender.sendLazy(msg, 6 * 1000);
    }
}
