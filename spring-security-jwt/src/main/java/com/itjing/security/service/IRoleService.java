package com.itjing.security.service;

import com.itjing.security.entity.RoleEntity;

import java.util.List;

/**
 * @author lijing
 * @date 2022年06月09日 20:25
 * @description 角色管理接口
 */
public interface IRoleService {

    List<RoleEntity> getRoles(String userName);
}
