package com.itjing.thread.phaser;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * @Description:
 * @Author: lijing
 * @CreateTime: 2022-10-08 11:13
 */
@Slf4j
public class PhaserTest {

    /**
     * 和其他一些样例一样，它也是 Java Concurrent 包的元素，被称为 Phaser。
     * 它与更知名的 CountDownLatch 相当相似。然而，它提供了一些额外的功能。它允许我们设置在继续执行之前需要等待的线程的动态数量。
     * 在 Phaser 中，已定义数量的线程需要在进入下一步执行之前在屏障上等待。得益于此，我们可以协调多个阶段的执行。
     * 在下面的例子中，我们设置了一个具有 50 个线程的屏障，在进入下一个执行阶段之前，需要到达该屏障。
     * 然后，我们创建一个线程，在 Phaser 实例上调用 arriveAndAwaitAdvance() 方法。它会一直阻塞线程，直到所有的 50 个线程都到达屏障。
     * 然后，它进入 phase-1，同样会再次调用 arriveAndAwaitAdvance() 方法。
     */
    public static void main(String[] args) {
        Phaser phaser = new Phaser(50);
        Runnable r = () -> {
            System.out.println("phase-0");
            phaser.arriveAndAwaitAdvance();
            System.out.println("phase-1");
            phaser.arriveAndAwaitAdvance();
            System.out.println("phase-2");
            phaser.arriveAndDeregister();
        };
        ExecutorService executor = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 50; i++) {
            executor.submit(r);
        }
        executor.shutdown();
        /**
         * 结果：
         * phase-0：50次
         * phase-1：50次
         * phase-2：50次
         */
    }
}
