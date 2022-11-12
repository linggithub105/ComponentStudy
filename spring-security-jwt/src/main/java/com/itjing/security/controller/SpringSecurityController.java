package com.itjing.security.controller;

import com.itjing.security.dto.UserDTO;
import com.itjing.security.jwt.JwtTokenUtil;
import com.itjing.security.service.IUserService;
import com.itjing.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lijing
 * @date 2022年06月09日 21:57
 * @description SpringSecurity控制器
 */
@RestController
public class SpringSecurityController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserService userService;;

    /**
     * 自定义登录逻辑 TODO
     * @param userDTO
     * @param response
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO, HttpServletResponse response) {

        // 通过用户名和密码创建一个 Authentication 认证对象，实现类为 UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDTO.getUserName(), userDTO.getPassword());
        // 通过 AuthenticationManager（默认实现为ProviderManager）的authenticate方法验证
        // Authentication 对象
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 将 Authentication 绑定到 SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成Token
        UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getUserName());
        String token = jwtTokenUtil.generateToken(userDetails);
        // 将Token写入到Http头部
        response.addHeader(jwtTokenUtil.getHeader(), token);

        return token;
    }

    @GetMapping("/getUserByUsername")
    public String getUserByUsername(String userName) {
        return "lijing";
    }

    @PutMapping("/user/update")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public String updateUser() {
        return "success";
    }

    @PutMapping("/user/update2")
    @PreAuthorize("hasAnyAuthority('user:update2')")
    public String updateUser2() {
        return "success";
    }


}
