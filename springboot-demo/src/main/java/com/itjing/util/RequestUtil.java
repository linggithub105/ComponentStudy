package com.itjing.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lijing
 * @date 2022年06月07日 14:44
 * @description 请求工具类
 */
public class RequestUtil {

    private static final String X_REAL_IP = "X-Real-IP";
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_FORWARDED_FOR_SPLIT_SYMBOL = ",";

    /**
     * get real client ip
     * first use X-Forwarded-For header
     * https://zh.wikipedia.org/wiki/X-Forwarded-For
     * next nginx X-Real-IP
     * last {@link HttpServletRequest#getRemoteAddr()}
     *
     * @param request {@link HttpServletRequest}
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader(X_FORWARDED_FOR);
        if (!StringUtils.isBlank(xForwardedFor)) {
            return xForwardedFor.split(X_FORWARDED_FOR_SPLIT_SYMBOL)[0].trim();
        }
        String nginxHeader = request.getHeader(X_REAL_IP);
        return StringUtils.isBlank(nginxHeader) ? request.getRemoteAddr() : nginxHeader;
    }
}
