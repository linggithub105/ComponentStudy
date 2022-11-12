package com.itjing.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author lijing
 * @date 2022年07月25日 9:21
 * @description
 */
public class AssertUtils {

    private AssertUtils() {
    }

    public static <T, E extends RuntimeException> void isTrue(T t, Predicate<T> verifier, Supplier<E> exSupplier) {
        // 断言结果
        boolean asserted = verifier.test(t);
        // 断言未通过
        if (!asserted) throw exSupplier.get();
    }

    public static <T, E extends RuntimeException> void isTrue(T caller, T reference, BiPredicate<T, T> verifier, Supplier<E> exSupplier) {
        // 断言结果
        boolean asserted = verifier.test(caller, reference);
        // 断言未通过
        if (!asserted) throw exSupplier.get();
    }

    /**********************************************以下为断言时的常用方法,提供静态方法引用 **********************************************/
    // 对象为空
    public static <T> Boolean isNull(T t) {
        return Objects.isNull(t);
    }

    // 对象非空
    public static <T> Boolean isNotNull(T t) {
        return !isNull(t);
    }

    // 比较后相等
    public static <T extends Comparable<T>> Boolean isEq(T caller, T reference) {
        return caller.compareTo(reference) == 0;
    }

    // 比较后大于
    public static <T extends Comparable<T>> Boolean isGt(T caller, T reference) {
        return caller.compareTo(reference) == 1;
    }

    // 比较后大于等于
    public static <T extends Comparable<T>> Boolean isGe(T caller, T reference) {
        return caller.compareTo(reference) >= 0;
    }

    // 比较后小于
    public static <T extends Comparable<T>> Boolean isLt(T caller, T reference) {
        return caller.compareTo(reference) == -1;
    }

    // 比较后小于等于
    public static <T extends Comparable<T>> Boolean isLe(T caller, T reference) {
        return caller.compareTo(reference) <= 0;
    }

    public static void main(String[] args) {
        // 异常抽取为供给型函数式接口,可以定制异常
        // 单个对象判空
        AssertUtils.isTrue(null, AssertUtils::isNotNull, () -> new RuntimeException("要求非空"));
        AssertUtils.isTrue(1, 1, AssertUtils::isEq, () -> new RuntimeException("要求相等"));
        AssertUtils.isTrue(1f, 2f, AssertUtils::isGt, () -> new RuntimeException("要求大于"));
        AssertUtils.isTrue(new Date(), new Date(), AssertUtils::isLt, () -> new RuntimeException("要求小于"));

        AssertUtils.isTrue(1l, 2l, AssertUtils::isLe, () -> new RuntimeException("要求xxx"));
        AssertUtils.isTrue(1d, 2d, AssertUtils::isLe, () -> new RuntimeException("要求xxx"));
        AssertUtils.isTrue(new BigDecimal(1), new BigDecimal(2), AssertUtils::isLe, () -> new RuntimeException("要求xxx"));
    }
}
