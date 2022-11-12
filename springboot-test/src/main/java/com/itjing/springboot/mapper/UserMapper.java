package com.itjing.springboot.mapper;

import com.itjing.springboot.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lijing
 * @date 2022年07月03日 17:57
 * @description
 */
@Mapper
public interface UserMapper {

    int getUserCount(@Param("user") User user);

}
