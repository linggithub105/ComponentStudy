package com.itjing.security.config;

import com.alibaba.fastjson.JSON;
import com.itjing.security.jwt.JwtAccessDeniedHandler;
import com.itjing.security.jwt.JwtAuthenticationEntryPoint;
import com.itjing.security.jwt.JwtAuthenticationFilter;
import com.itjing.security.jwt.JwtTokenUtil;
import com.itjing.security.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lijing
 * @date 2022年06月09日 19:47
 * @description SpringSecurity配置类
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Slf4j
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 当用户在没有授权的时候，返回的指定信息
     */
    @Resource
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /**
     * 用户访问资源没有携带正确的token时返回的信息
     */
    @Resource
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * Jwt认证过滤器
     */
    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 这边 通过重写configure()，去数据库查询用户是否存在
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                // 自定义登录拦截URI
                // loginProcessingUrl和loginPage好像要同时配置
                // 否则会出现问题，具体原因debug源码可得知
                .loginProcessingUrl("/loginReq")
                .loginPage("/loginPage")
                .usernameParameter("userName")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    log.info("onAuthenticationSuccess: {}", authentication.getName());
                    String token = jwtTokenUtil.generateToken(authentication.getName());
                    response.setContentType("application/json;charset=utf-8");
                    Map<String, Object> map = new HashMap<>();
                    // 这里写死测试，具体以实际为主
                    map.put("code", 200);
                    map.put("message", "登录成功！");
                    map.put("token", token);
                    response.addHeader(jwtTokenUtil.getHeader(), token);
                    response.getWriter().println(JSON.toJSONString(map));
                    response.getWriter().flush();
                    response.getWriter().close();
                })
                .failureHandler((request, response, e) -> {
                    log.info("onAuthenticationFailure: {}", e.getMessage());
                    response.setContentType("text/json;charset=utf-8");
                    Map<String, Object> map = new HashMap<>();
                    // 这里写死测试，具体以实际为主
                    map.put("code", 500);
                    map.put("message", "登录失败！");
                    map.put("failReason", e.getMessage());
                    response.getWriter().println(JSON.toJSONString(map));
                    response.getWriter().flush();
                    response.getWriter().close();
                })
                .and()
                // token的验证方式不需要开启csrf的防护
                .csrf().disable()
                // 自定义认证失败类
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                // 自定义权限不足处理类
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                // 设置无状态的连接,即不创建session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/login").permitAll()
                // 配置允许匿名访问的路径
                .anyRequest()
                .authenticated();

        // 解决跨域问题（重要）只有在前端请求接口时才需要这个
        http.cors()
                .and()
                .csrf()
                .disable();

        // 配置自己的jwt验证过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        


        // disable page caching
        http.headers().cacheControl();

    }
}
