package com.itjing.multi.service;

import com.itjing.multi.mapper.db1.TestDB1Mapper;
import com.itjing.multi.mapper.db2.TestDB2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lijing
 * @date 2022年06月15日 20:23
 * @description 业务层
 */
@Service
public class TestService {

    @Autowired
    private TestDB1Mapper testDB1Mapper;

    @Autowired
    private TestDB2Mapper testDB2Mapper;

    public List<Map<String, Object>> testDB1() {
        return testDB1Mapper.selectDB1();
    }

    public List<Map<String, Object>> testDB2() {
        return testDB2Mapper.selectDB2();
    }

}
