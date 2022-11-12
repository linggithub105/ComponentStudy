package com.itjing.thread.stampedlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * @Description:
 * @Author: lijing
 * @CreateTime: 2022-10-08 10:05
 */
@Slf4j
public class StampedLockTest {

    /**
     * 我认为，Java Concurrent 是最有趣的 Java 包之一。同时，它也是一个不太为开发者所熟知的包，
     * 当开发人员主要使用 web 框架的时候更是如此。我们有多少人曾经在 Java 中使用过锁呢？
     * 锁是一种比 synchronized 块更灵活的线程同步机制。从 Java 8 开始，我们可以使用一种叫做 StampedLock 的新锁。
     * StampedLock 是 ReadWriteLock 的一个替代方案。它允许对读操作进行乐观的锁定。
     * 而且，它的性能比 ReentrantReadWriteLock 更好。
     * 假设我们有两个线程。第一个线程更新一个余额，而第二个线程则读取余额的当前值。为了更新余额，我们当然需要先读取其当前值。
     * 在这里，我们需要某种同步机制，假设第一个线程在同一时间内多次运行。第二个线程阐述了如何使用乐观锁来进行读取操作。
     */
    public static void main(String[] args) throws InterruptedException {
        StampedLock lock = new StampedLock();
        Balance b = new Balance(10000);
        Runnable w = () -> {
            long stamp = lock.writeLock();
            b.setAmount(b.getAmount() + 1000);
            log.info("Write: " + b.getAmount());
            lock.unlockWrite(stamp);
        };
        Runnable r = () -> {
            long stamp = lock.tryOptimisticRead();
            if (!lock.validate(stamp)) {
                stamp = lock.readLock();
                try {
                    log.info("Read: " + b.getAmount());
                } finally {
                    lock.unlockRead(stamp);
                }
            } else {
                log.info("Optimistic read fails");
            }
        };
        // 现在，我们同时运行这两个线程 50 次。它的结果应该是符合预期的，最终的余额是 60000。
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 50; i++) {
            executor.submit(w);
            executor.submit(r);
        }
        executor.shutdown();
        if (executor.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
            log.info("Balance: " + b.getAmount());
        }
    }
}
