package com.itjing.multi.service;

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

    public List<Map<String, Object>> getDeptList() {
        return testMapper.selectDept();
    }
}
