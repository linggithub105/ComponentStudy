package com.itjing.springboot.controller;

import com.itjing.springboot.entity.User;
import com.itjing.springboot.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijing
 * @date 2022年07月03日 17:39
 * @description
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getUser")
    public User get(@ModelAttribute User user) {
        System.out.println(user);
        System.out.println(userMapper.getUserCount(user));
        return user;
    }

    @GetMapping("/testIdeaShortcuts")
    public String testIdeaShortcuts() {
        return "success";
    }
}
