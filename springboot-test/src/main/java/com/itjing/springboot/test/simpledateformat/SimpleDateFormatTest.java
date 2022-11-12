package com.itjing.springboot.test.simpledateformat;

import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lijing
 * @date 2022年07月20日 19:16
 * @description
 */
public class SimpleDateFormatTest {

    private static FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        // 1、创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(5);
        // 2、为线程池分配任务
        ThreadPoolTest threadPoolTest = new ThreadPoolTest();
        for (int i = 0; i < 100; i++) {
            pool.submit(threadPoolTest);
        }
        // 3、关闭线程池
        pool.shutdown();
    }


    static class ThreadPoolTest implements Runnable {

        @Override
        public void run() {
            try {
                String dateString = fastDateFormat.format(new Date());
                Date parseDate = fastDateFormat.parse(dateString);
                String formatString = fastDateFormat.format(parseDate);
                System.out.println(Thread.currentThread().getName() + " 线程是否安全: " + dateString.equals(formatString));
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + " 格式化失败 ");
            }
        }
    }
}