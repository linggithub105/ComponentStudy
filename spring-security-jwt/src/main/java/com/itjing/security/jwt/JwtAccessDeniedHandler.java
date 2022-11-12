package com.itjing.security.jwt;

import com.itjing.security.common.bean.Result;
import com.itjing.security.common.bean.ResultUtil;
import com.itjing.security.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author lijing
 * @date 2022年06月09日 21:00
 * @description 当用户在没有授权的时候，返回的指定信息
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws ServletException {
        log.info("用户访问没有授权资源: {}", e.getMessage());
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        try (PrintWriter out = response.getWriter();) {
            Result result = ResultUtil.fail("抱歉，用户暂无权限访问该资源！").setCode(HttpServletResponse.SC_UNAUTHORIZED);
            out.write(JsonUtil.obj2String(result));
            out.flush();
        } catch (IOException exception) {
            log.error(exception.getMessage());
        }
    }
}
