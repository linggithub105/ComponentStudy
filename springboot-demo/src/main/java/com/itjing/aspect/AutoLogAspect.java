package com.itjing.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itjing.annotation.AutoLog;
import com.itjing.entity.LogInfo;
import com.itjing.util.DateUtils;
import com.itjing.util.RequestUtil;
import com.itjing.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author lijing
 * @date 2022年06月07日 14:23
 * @description 自动记录系统日志切面
 */
@Aspect
@Component
@Slf4j
public class AutoLogAspect {

    @Pointcut("@annotation(com.itjing.annotation.AutoLog)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = point.proceed();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        // 保存日志，数据库操作，需要有日志表
        saveLog(point, time);
        return result;
    }

    /**
     * 保存日志
     *
     * @param point
     * @param time
     */
    private void saveLog(ProceedingJoinPoint point, long time) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        AutoLog autoLog = method.getAnnotation(AutoLog.class);
        LogInfo logInfo = new LogInfo();
        if (Objects.nonNull(autoLog)) {
            if (log.isDebugEnabled()) {
                log.info(autoLog.value() + autoLog.logType().name());
            }
            // 设置日志类型
            logInfo.setLogType(autoLog.logType().name());
            // 设置日志内容
            logInfo.setLogContent(autoLog.value());
        }
        // 请求的方法名
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();

        // 请求的参数
        List<Object> argList = new ArrayList<>();
        Object[] args = point.getArgs();
        for (Object arg : args) {
            if (arg instanceof ServletResponse || arg instanceof ServletRequest || arg instanceof InputStreamSource) {
                continue;
            }
            argList.add(arg);
        }

        // 此处使用了SpringSecurity的Authentication对象，可以获取到当前登录用户的信息
        // 需要项目里集成SpringSecurity
        /*SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = null;
        if (context != null) {
            authentication = context.getAuthentication();
        }
        String currentUsername = "";
        if (authentication != null) {
            currentUsername = authentication.getName();
        }*/

        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        // id
        logInfo.setId(UUID.randomUUID().toString().replace("-", "").toUpperCase());
//        logInfo.setUsername(currentUsername);
        // 花费时间
        logInfo.setCostTime(Long.valueOf(time));
        // ip地址
        logInfo.setIpAddr(RequestUtil.getRemoteIp(request));
        // 请求的方法完整名
        logInfo.setMethod(className + "." + methodName + "()");
        logInfo.setRequestUrl(request.getRequestURI());
        // 请求类型
        logInfo.setRequestType(request.getMethod());
        // 请求参数
        logInfo.setRequestParam(JSON.toJSONString(argList));
//        logInfo.setCreateUser(currentUsername);
        // 创建时间
        logInfo.setCreateTime(DateUtils.getDateTimeStr());

        // 日志保存到数据库 TODO
        log.info(JSONObject.toJSONString(logInfo));
    }
}
