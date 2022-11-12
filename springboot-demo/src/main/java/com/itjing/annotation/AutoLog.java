package com.itjing.annotation;

import com.itjing.enu.LogType;

import java.lang.annotation.*;

/**
 * @author lijing
 * @date 2022年06月07日 14:25
 * @description 系统日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLog {


    /**
     * 日志内容
     *
     * @return
     */
    String value() default "";

    /**
     * 日志类型
     *
     * @return 0:操作日志; 1:登录日志; 2:定时任务;
     */
    LogType logType() default LogType.query;

}
