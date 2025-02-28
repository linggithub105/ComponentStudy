package com.itjing.activiti.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 用户控制器
 * @Author: lijing
 * @CreateTime: 2022-09-09 15:56
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/common")
    public String common() {
        return "hello~ common";
    }

    @GetMapping("/admin")
    public String admin() {
        return "hello~ admin";
    }

    @GetMapping("/test")
    public String test() {
        return "hello~ test";
    }

    @GetMapping("/hello")
    public String hello(@RequestParam("code") String code) {
        return "hello~  授权码 code 为：" + code;
    }

}