package com.itjing.multi.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.itjing.multi.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lijing
 * @date 2022年06月16日 20:49
 * @description
 */
@Service
public class TestService {

    @Autowired
    private TestMapper testMapper;

    @DS("db1")
    public List<Map<String, Object>> getDeptListDb1() {
        return testMapper.selectDept();
    }

    @DS("db2")
    public List<Map<String, Object>> getDeptListDb2() {
        return testMapper.selectDept();
    }
}
