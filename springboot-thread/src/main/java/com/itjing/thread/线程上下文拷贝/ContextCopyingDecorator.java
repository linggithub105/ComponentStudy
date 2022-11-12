package com.itjing.thread.线程上下文拷贝;

import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @Description: 线程上下文拷贝
 * @Author: lijing
 * @CreateTime: 2022-09-30 09:48
 */
public class ContextCopyingDecorator implements TaskDecorator {

    /**
     * 对于异常操作中线程切换，有时候需要的用户信息就没有了，所以需要处理线程上下文拷贝
     *
     * @param runnable the original {@code Runnable}
     * @return
     */
    @Override
    public Runnable decorate(Runnable runnable) {
        RequestAttributes context = RequestContextHolder.currentRequestAttributes();
        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(context);
                runnable.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        };
    }
}