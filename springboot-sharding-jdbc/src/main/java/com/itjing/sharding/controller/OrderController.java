package com.itjing.sharding.controller;

import com.itjing.sharding.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lijing
 * @date 2022年06月19日 11:20
 * @description
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;

    @PostMapping("/save")
    public String testInsertOrder() {
        for (int i = 0; i < 10; i++) {
            orderMapper.insertOrder(100 + i, "空调" + i, 10);
        }
        return "success";
    }

    @GetMapping("find")
    public void testFindOrderByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(745241267423674369L);
        ids.add(745241268338032640L);

        List<Map> list = orderMapper.findOrderByIds(ids);
        System.out.println(list);
    }


    @PostMapping("/saveFk")
    public String saveFk() {
        for (int i = 0; i < 10; i++) {
            orderMapper.insertOrderFk(i, "空调" + i, 1);
        }
        return "success";
    }


    @GetMapping("findFk")
    public void testFindOrderByIdsFk() {
        List<Long> ids = new ArrayList<>();
        ids.add(745252093001990145L);
        ids.add(745252094096703488L);

        List<Map> list = orderMapper.findOrderByIdsFk(ids);
        System.out.println(list);
    }
}
