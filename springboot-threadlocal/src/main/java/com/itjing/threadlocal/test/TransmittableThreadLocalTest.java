package com.itjing.threadlocal.test;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author lijing
 * @date 2022年06月04日 11:09
 * @description TransmittableThreadLocal的使用
 * TransmittableThreadLocal 是Alibaba开源的用于解决 “在使用线程池等会缓存线程的组件情况下传递ThreadLocal” 问题的 InheritableThreadLocal 扩展。
 * ThreadLocal只在当前线程中能访问到，其他线程隔离。
 * InheritableThreadLocal能够传递给子线程。
 * 对于线程池中的线程复用，将当前线程中的ThreadLocal传递给线程池中的其他线程，TransmittableThreadLocal提供了解决方案。
 */
public class TransmittableThreadLocalTest {

    public static void main(String[] args) throws InterruptedException {
        // 创建一个单一线程池，核心线程池为1，由于核心线程数是1，势必存在线程的复用
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // 使用TtlExecutors对线程池包装一下
        executorService = TtlExecutors.getTtlExecutorService(executorService);
        // 使用InheritableThreadLocal存储
        // ThreadLocal<String> value = new InheritableThreadLocal<>();
        // 使用TransmittableThreadLocal存储
        ThreadLocal<String> value = new TransmittableThreadLocal<>();
        for (int i = 0; i < 10; i++) {
            value.set("xiaojinggege—" + i);
            TimeUnit.MILLISECONDS.sleep(3000);
            CompletableFuture.runAsync(() -> System.out.println(value.get()), executorService);
        }
    }

}
