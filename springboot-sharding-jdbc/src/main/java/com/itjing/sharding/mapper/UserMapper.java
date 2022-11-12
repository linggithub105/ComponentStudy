package com.itjing.sharding.mapper;

import com.itjing.sharding.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lijing
 * @date 2022年06月19日 10:46
 * @description
 */
@Mapper
public interface UserMapper {

    @Insert("insert into t_user(nickname,password,sex,birthday) values(#{nickname},#{password},#{sex},#{birthday})")
    void addUser(User user);

    @Select("select * from t_user")
    List<User> findUsers();
}