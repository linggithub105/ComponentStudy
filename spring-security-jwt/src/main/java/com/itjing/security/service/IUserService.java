package com.itjing.security.service;

import com.itjing.security.entity.ResourceEntity;
import com.itjing.security.entity.UserEntity;

import java.util.List;

/**
 * @author lijing
 * @date 2022年06月09日 20:27
 * @description 用户管理接口
 */
public interface IUserService {

    List<ResourceEntity> getResourceList(String userName);

    UserEntity getUserByUsername(String userName);
}
