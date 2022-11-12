package com.itjing.swagger3.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijing
 * @date 2022年08月11日 16:15
 * @description
 */
@RequestMapping("/test")
@RestController
@Api(tags = "测试")
public class TestController {

    @GetMapping("/test")
    @ApiOperation("测试")
    public String test() {
        return "test";
    }
}
