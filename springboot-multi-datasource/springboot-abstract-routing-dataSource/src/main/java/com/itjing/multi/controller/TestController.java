package com.itjing.multi.controller;

import com.itjing.multi.annotation.WithDataSource;
import com.itjing.multi.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author lijing
 * @date 2022年06月16日 20:57
 * @description
 */
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/getDeptListDb1")
    @WithDataSource("db1")
    public List<Map<String, Object>> getDeptListDb1() {
        List<Map<String, Object>> deptList = testService.getDeptList();
        return deptList;
    }

    @GetMapping("/getDeptListDb2")
    @WithDataSource("db2")
    public List<Map<String, Object>> getDeptListDb2() {
        List<Map<String, Object>> deptList = testService.getDeptList();
        return deptList;
    }
}
