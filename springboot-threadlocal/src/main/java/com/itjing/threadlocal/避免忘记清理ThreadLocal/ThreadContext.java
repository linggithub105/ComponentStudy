package com.itjing.threadlocal.避免忘记清理ThreadLocal;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @Description: 封装一个工具类，实现避免忘记清理ThreadLocal
 * @Author: lijing
 * @CreateTime: 2022-10-08 09:39
 */
public class ThreadContext {

    private static final ThreadLocal<Map<String, Object>> CONTEXT = new TransmittableThreadLocal<>();

    /**
     * 初始化上下文
     */
    private static void initContext() {
        Map<String, Object> con = CONTEXT.get();
        if (con == null) {
            CONTEXT.set(new HashMap<>(8));
        } else {
            CONTEXT.get().clear();
        }
    }

    /**
     * 清除上下文
     */
    private static void clearContext() {
        CONTEXT.remove();
    }

    /**
     * 获取上下文内容
     */
    public static <T> T getValue(String key) {
        Map<String, Object> con = CONTEXT.get();
        if (con == null) {
            return null;
        }
        return (T) con.get(key);
    }

    /**
     * 设置上下文参数
     */
    public static void putValue(String key, Object value) {
        Map<String, Object> con = CONTEXT.get();
        if (con == null) {
            CONTEXT.set(new HashMap<>(8));
            con = CONTEXT.get();
        }
        con.put(key, value);
    }

    /**
     * 自动回收的封装
     */
    public static void runWithAutoClear(Runnable runnable) {
        initContext();
        try {
            runnable.run();
        } finally {
            CONTEXT.remove();
        }
    }

    /**
     * 自动回收的封装
     */
    public static <T> T callWithAutoClear(Callable<T> callable) {
        initContext();
        try {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } finally {
            CONTEXT.remove();
        }
    }

    public static void main(String[] args) {
        // 使用方法
        ThreadContext.runWithAutoClear(() -> {
            // 需要执行的代码逻辑
            System.out.println("this is code logic...");
        });
    }

}
