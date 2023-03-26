package com.itjing.service.impl;

import com.itjing.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @program: 线程池配置类
 * @author: zhanling.li
 * @create: 2022-06-02 10:26
 */
@Service
@Slf4j
public class AsyncServiceImpl implements AsyncService {

    @Override
    @Async("asyncServiceExecutor")
    public void executeAsync() {
        log.info("start executeAsync");
        // 异步线程可以在这里执行操作比较耗时的事情 TODO

        log.info("do something......");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("end executeAsync");
    }
}
