package com.itjing.controller;

import com.itjing.entity.CheckBatch;
import com.itjing.log.annotation.AspectLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lijing
 * @date 2022年03月11日 10:21
 * @description
 */
@RestController
public class TestController {

    @PostMapping("/auth/login")
    @AspectLog
    public Map<String, Object> login() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "登录成功");
        map.put("data", "token");
        return map;
    }

    @GetMapping("/auth/info")
    @AspectLog
    public Map<String, Object> info() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "success");
        Map<String, Object> data = new HashMap<>();
        data.put("username", "lijing");
        map.put("data", data);
        return map;
    }

    @GetMapping("/checktask/appSelectAll")
    public Map<String, Object> appSelectAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "success");
        List<CheckBatch> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            CheckBatch checkBatch = new CheckBatch();
            checkBatch.setId(i + "");
            checkBatch.setTaskName("任务" + i);
            checkBatch.setReadyCount(3 + "");
            checkBatch.setAlreadyCount(2 + "");
            data.add(checkBatch);
        }
        map.put("data", data);
        return map;
    }
}
