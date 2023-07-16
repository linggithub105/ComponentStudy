package com.itjing.tree;

import java.util.Map;

/**
 * @Description: 定义一个函数式接口
 * @Author: lijing
 * @CreateTime: 2022-11-15 15:18
 *
 * 泛型说明：
 *
 * N 表示传入回调函数的结点参数类型
 *
 * K 表示结果集的key
 *
 * V 表示结果集的value
 */
@FunctionalInterface
public interface FunctionHandle<N, K, V> {

    void callback(N node, Map<K, V> result);

}