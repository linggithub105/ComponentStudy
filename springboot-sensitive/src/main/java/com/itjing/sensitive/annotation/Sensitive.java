package com.itjing.sensitive.annotation;

import com.itjing.sensitive.enums.SensitiveEnum;

import java.lang.annotation.*;

/**
 * @Description: 脱敏注解
 * @Author: lijing
 * @CreateTime: 2022-11-03 11:00
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sensitive {

    SensitiveEnum mask() default SensitiveEnum.NO_MASK;

    String maskChar() default "*";

}
