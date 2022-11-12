package com.itjing.flowable;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @Description: 发送失败提示
 * @Author: lijing
 * @CreateTime: 2022-09-09 10:48
 */
@Slf4j
public class AskForLeaveFail implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.info("请假失败...............");
    }
}
