package com.itjing.api.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: Http调用处理器
 * @Author: lijing
 * @CreateTime: 2022-10-09 17:58
 */
@RestController
@RequestMapping("/http/call")
@Slf4j
public class HttpCallController {

    @PostMapping("/start")
    public String rest() {
        String url = "http://127.0.0.1:9876/http/call/rest";
        Map<String, Object> params = new HashMap<>();
        params.put("username", "lijing");
        params.put("password", "lijing123");
        HttpResponse result = HttpRequest.post(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .charset("UTF-8")
                .form(params)
                .timeout(20000)
                .execute();
        return result.body();
    }

    @PostMapping("/rest")
    public Map<String, Object> rest(String username, String password) {
        Map<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("password", password);
        return result;
    }
}
