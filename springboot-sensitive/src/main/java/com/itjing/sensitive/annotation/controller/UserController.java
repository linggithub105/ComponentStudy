package com.itjing.sensitive.annotation.controller;

import com.itjing.sensitive.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 用户控制器
 * @Author: lijing
 * @CreateTime: 2022-11-03 11:16
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @GetMapping("/findUsers")
    public List<User> findUsers() {
        List<User> list = new ArrayList<>();
        list.add(new User(1L, "lijing", 18, "2427259171@qq.com"));
        return list;
    }

}
