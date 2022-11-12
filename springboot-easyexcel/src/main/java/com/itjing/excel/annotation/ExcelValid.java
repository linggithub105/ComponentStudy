package com.itjing.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: Excel导入字段校验，这里暂定为必填校验
 * @Author: lijing
 * @CreateTime: 2022-09-07 14:23
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelValid {
    String message() default "";
}