package com.itjing.thread.accumulator;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * @Description: 并发累加器Api测试
 * @Author: lijing
 * @CreateTime: 2022-10-08 10:34
 */
@Slf4j
public class AccumulatorTest {

    /**
     * 在 Java Concurrent 包中，有意思的并不仅仅有锁，另外一个很有意思的东西是并发累加器（concurrent accumulator）。
     * 我们也有并发的加法器（concurrent adder），但它们的功能非常类似。LongAccumulator（我们也有 DoubleAccumulator）会使用一个提供给它的函数更新一个值。
     * 在很多场景下，它能让我们实现无锁的算法。当多个线程更新一个共同的值的时候，它通常会比 AtomicLong 更合适。
     * 我们看一下它是如何运行的。要创建它，我们需要在构造函数中设置两个参数。第一个参数是一个用于计算累加结果的函数。
     * 通常情况下，我们会使用 sum 方法。第二个参数表示累积器的初始值。
     * 现在，让我们创建一个初始值为 10000 的 LongAccumulator，然后从多个线程调用 accumulate() 方法。最后的结果是什么呢？
     * 这里面并没有用到锁！
     */

    public static void main(String[] args) throws Exception {
        //testLongAccumulator();
        testDoubleAccumulator();
    }

    public static void testLongAccumulator() throws Exception {
        LongAccumulator balance = new LongAccumulator(Long::sum, 10000L);
        Runnable w = () -> balance.accumulate(1000L);
        ExecutorService executor = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 50; i++) {
            executor.submit(w);
        }
        executor.shutdown();
        if (executor.awaitTermination(1000L, TimeUnit.MILLISECONDS)) {
            log.info("Balance: " + balance.get());
        }
        assert balance.get() == 60000L;
    }

    public static void testDoubleAccumulator() throws Exception {
        DoubleAccumulator balance = new DoubleAccumulator(Double::sum, 10000.0);
        Runnable w = () -> balance.accumulate(1000.0);
        ExecutorService executor = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 50; i++) {
            executor.submit(w);
        }
        executor.shutdown();
        if (executor.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
            log.info("Balance: " + balance.get());
        }
        // 断言，如果值不符合预期，则抛异常输出后面的提示信息
        assert balance.get() == 60000.0 : "不对，值应该为60000.0才对";
    }
}
