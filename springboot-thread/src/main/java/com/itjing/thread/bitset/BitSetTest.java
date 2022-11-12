package com.itjing.thread.bitset;

import lombok.extern.slf4j.Slf4j;

import java.util.BitSet;

/**
 * @Description:
 * @Author: lijing
 * @CreateTime: 2022-10-08 10:56
 */
@Slf4j
public class BitSetTest {

    /**
     * 如果我们需要对 bit 数组进行一些操作该怎么办呢？你是不是会使用 boolean[] 来实现呢？
     * 其实，有一种更有效、更节省内存的方法来实现。这就是 BitSet 类。BitSet 类允许我们存储和操作 bit 的数组。
     * 与 boolean[] 相比，它消耗的内存要少 8 倍。我们可以对数组进行逻辑操作，例如：and、or、xor。
     * 比方说，有两个 bit 的数组, 我们想对它们执行 xor 操作。为了做到这一点，我们需要创建两个 BitSet 的实例，
     * 并在实例中插入样例元素，如下所示。最后，对其中一个 BitSet 实例调用 xor 方法，并将第二个 BitSet 实例作为参数。
     */
    public static void main(String[] args) {
        /**
         * int nextSetBit(int startIndex) // 返回第一个设置为 true 的位的索引，在指定的起始索引或之后的索引上
         * int nextClearBit(int startIndex) // 返回第一个设置为 false 的位的索引 , 在指定的起始索引或之后的索引上
         * void set(int index, boolean v) // 将指定索引处的位设置为指定的值，将指定索引处的位设置为 true
         */
        BitSet bs1 = new BitSet();
        bs1.set(0, true);
        bs1.set(2, true);
        bs1.set(4, true);
        // 以上相当于 10101
        log.info("bs1 : " + bs1);

        BitSet bs2 = new BitSet();
        bs2.set(1, true);
        bs2.set(2, true);
        bs2.set(3, true);
        // 以上相当于 01110
        log.info("bs2 : " + bs2);

        bs2.xor(bs1);
        /**
         * 10101
         * 01110
         * ------
         * 11011
         * 即 0,1,3,4
         */
        log.info("xor: " + bs2);

        /**
         * 运行结果：
         * bs1 : {0, 2, 4}
         * bs2 : {1, 2, 3}
         * xor: {0, 1, 3, 4}
         */
    }
}
