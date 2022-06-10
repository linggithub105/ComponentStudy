package com.itjing.log.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author lijing
 * @date 2022年06月02日 14:45
 * @description 日志切面自动配置
 */
@Aspect
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@Configuration
// 当配置文件有 aspectlog.enable = true 时开启，如果配置文件没有设置 aspectlog.enable也开启。
@ConditionalOnProperty(
        prefix = "aspectlog",
        name = "enable",
        havingValue = "true",
        matchIfMissing = true
)
public class AspectLogAutoConfiguration {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 切面环绕通知，记录日志，根据实际情况自行修改
     * @param thisJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.itjing.log.annotation.AspectLog)")
    public Object aspectLog(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        // 执行方法名称
        String taskName = thisJoinPoint.getSignature()
                .toString()
                .substring(
                        thisJoinPoint.getSignature().toString().indexOf(" "),
                        thisJoinPoint.getSignature().toString().indexOf("(")
                );
        taskName = taskName.trim();
        long time = System.currentTimeMillis();
        Object result = thisJoinPoint.proceed();
        logger.info("method: [{}]  run: [{}] ms", taskName, (System.currentTimeMillis() - time));
        return result;
    }
}
