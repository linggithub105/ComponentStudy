package com.itjing.multi.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author lijing
 * @date 2022年06月16日 20:45
 * @description
 */
@Mapper
public interface TestMapper {

    List<Map<String, Object>> selectDept();

}
