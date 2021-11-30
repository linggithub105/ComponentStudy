package com.itjing.juc.unsafe;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lijing
 * @Description
 * @create 2020-11-20 12:05
 */
public class MapTest {
    public static void main(String[] args) {
        // map 是这样用的吗？ 不是，工作中不用 HashMap
        // 默认等价于什么？  new HashMap<>(16,0.75);
        // Map<String, String> map = new HashMap<>();
        // 唯一的一个家庭作业：研究ConcurrentHashMap的原理
        //Map<String, String> map = Collections.synchronizedMap(new HashMap<>());
        Map<String, String> map = new ConcurrentHashMap<>();
        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0, 5));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }

    }
}
