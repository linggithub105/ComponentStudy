package com.itjing.security.jwt;

import com.itjing.security.common.bean.Result;
import com.itjing.security.common.bean.ResultUtil;
import com.itjing.security.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author lijing
 * @date 2022年06月09日 19:49
 * @description 没有token时，Jwt的EntryPoint
 * 用户访问资源没有携带正确的token时返回的信息
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws ServletException, IOException, IOException {

        log.error("Responding with unauthorized error. Message:{}, url:{}", e.getMessage(), request.getRequestURI());
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Result result = ResultUtil.fail("访问受保护资源，用户未通过认证，请重新登录！").setCode(HttpServletResponse.SC_UNAUTHORIZED);
            out.write(JsonUtil.obj2String(result));
            out.flush();
        } catch (IOException exception) {

        }
    }
}
