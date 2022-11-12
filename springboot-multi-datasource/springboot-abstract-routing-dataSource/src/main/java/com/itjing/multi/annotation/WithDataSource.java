package com.itjing.multi.annotation;

import java.lang.annotation.*;

/**
 * @author lijing
 * @date 2022年06月16日 21:32
 * @description 数据源切换注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithDataSource {
    String value() default "";
}