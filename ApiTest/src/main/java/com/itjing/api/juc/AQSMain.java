package com.itjing.api.juc;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AQSMain {



    @Test
    public void testQueue() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        new Thread(()->{
            CountDownLatch countDownLatch = new CountDownLatch(10);
            for (int i = 0; i < 10; i++) {
                RunThread runThread = new RunThread(countDownLatch);
                new Thread(runThread).start();
            }
            try {
                countDownLatch.await();
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        }).start();
        TimeUnit.SECONDS.sleep(10);
        System.out.println("主线程.............");

    }

    static class RunThread implements Runnable{
        private CountDownLatch countDownLatch;

        public RunThread(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
            System.out.println("我也不知道我在干什么");
        }
    }

}
