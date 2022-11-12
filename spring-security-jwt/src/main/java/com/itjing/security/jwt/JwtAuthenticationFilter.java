package com.itjing.security.jwt;

import com.itjing.security.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lijing
 * @date 2022年06月09日 21:33
 * @description Jwt认证过滤器
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";

    private static final String X_ACCESS_TOKEN = "X-Access-Token";

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String token = resolveToken(request);
        log.info("header token: {}", token);
        // 如果请求头中有token，则进行解析，并且设置认证信息
        if (!StringUtils.isEmpty(token)) {
            // 根据token获取用户名
            String userName = jwtTokenUtil.getSubjectFromToken(token);
            log.info("userName: {}", userName);
            // userName，如果验证合法则保存到SecurityContextHolder
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                // JWT验证通过，使用 Spring Security 管理
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    // 加载用户、角色、权限信息
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userName, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        // 如果请求头中没有Authorization信息则直接放行
        chain.doFilter(request, response);
    }


    /**
     * Get token from header
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtTokenUtil.getHeader());
        if (!StringUtils.isEmpty(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        String jwt = request.getParameter(X_ACCESS_TOKEN);
        if (!StringUtils.isEmpty(jwt)) {
            return jwt;
        }
        return null;
    }

}
