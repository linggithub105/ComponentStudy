package com.itjing.activiti.mapper;

import com.itjing.activiti.entity.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: 权限持久层
 * @Author: lijing
 * @CreateTime: 2022-09-09 15:14
 */

public interface PermissionMapper {


    /**
     * 查询用户的权限根据用户查询权限
     *
     * @return
     */
    @Select("select * from permission")
    List<Permission> findAllPermission();

}