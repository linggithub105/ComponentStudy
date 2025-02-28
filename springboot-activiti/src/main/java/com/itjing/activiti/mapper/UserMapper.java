package com.itjing.activiti.mapper;

import com.itjing.activiti.entity.Permission;
import com.itjing.activiti.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: 用户持久层
 * @Author: lijing
 * @CreateTime: 2022-09-09 15:18
 */
public interface UserMapper {

    /**
     * 根据用户名称查询
     *
     * @param userName
     * @return
     */
    @Select(" select * from user where username = #{userName}")
    User findByUsername(@Param("userName") String userName);

    /**
     * 查询用户的权限根据用户查询权限
     *
     * @param userName
     * @return
     */
    @Select(" SELECT d.*\n" +
            "from user a,user_role b,role_permission c,permission d\n" +
            "WHERE \n" +
            "a.id = b.user_id\n" +
            "and b.role_id = c.role_id\n" +
            "and c.permission_id = d.id\n" +
            "and \n" +
            "a.username= #{userName};")
    List<Permission> findPermissionByUsername(@Param("userName") String userName);
}
