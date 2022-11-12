package com.itjing.api.stopwatch;


import org.junit.Test;
import org.springframework.util.StopWatch;

/**
 * @Description: StopWatch测试
 * @Author: lijing
 * @CreateTime: 2022-09-22 09:30
 */
public class StopWatchTest {

    @Test
    public void testSpringStopWatch() throws InterruptedException {
        // 创建一个 StopWatch 实例
        StopWatch sw = new StopWatch("jinggege");
        // 开始计时
        sw.start("任务1");

        Thread.sleep(1000);

        // 停止计时
        sw.stop();
        System.out.printf("任务1耗时：%d%s.\n", sw.getLastTaskTimeMillis(), "ms");

        sw.start("任务2");
        Thread.sleep(1100);
        sw.stop();

        System.out.printf("任务2耗时：%d%s.\n", sw.getLastTaskTimeMillis(), "ms");
        System.out.printf("任务数量：%s，总耗时：%ss.\n", sw.getTaskCount(), sw.getTotalTimeSeconds());

        System.out.println();
        System.out.println("--------------------------------------------------------");
        // 漂亮的格式化结果
        System.out.println(sw.prettyPrint());
    }

    @Test
    public void testApacheStopWatch() throws InterruptedException {
        // commons-lang3 可以通过 createStarted（创建并立即启动）、create（创建）来完成。
        // 还可以调用 suspend 方法暂停计时、resume 方法恢复计时、reset 重新计时。

        org.apache.commons.lang3.time.StopWatch sw = org.apache.commons.lang3.time.StopWatch.createStarted();
        Thread.sleep(1000);
        System.out.printf("耗时：%dms.\n", sw.getTime());

        // 暂停计时
        sw.suspend();
        System.out.printf("暂停耗时：%dms.\n", sw.getTime());

        // 恢复计时
        sw.resume();
        System.out.printf("恢复耗时：%dms.\n", sw.getTime());

        // 停止计时
        sw.stop();
        System.out.printf("总耗时：%dms.\n", sw.getTime());

        // 重置计时
        sw.reset();

        // 开始计时
        sw.start();
        System.out.printf("重置耗时：%dms.\n", sw.getTime());

    }

}
