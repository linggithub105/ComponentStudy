package com.itjing.security.service.impl;

import com.itjing.security.entity.ResourceEntity;
import com.itjing.security.entity.UserEntity;
import com.itjing.security.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lijing
 * @date 2022年06月09日 21:26
 * @description 用户业务实现类
 */
@Service
public class IUserServiceImpl implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<ResourceEntity> getResourceList(String userName) {
        // 这里我直接写死了，真实业务场景，到数据库去取
        // 一般根据用户的角色信息，然后获取资源权限信息
        List<ResourceEntity> resourseList = new ArrayList<>();
        ResourceEntity resource = new ResourceEntity();
        resource.setId("user");
        resource.setName("update");
        resourseList.add(resource);
        return resourseList;
    }

    @Override
    public UserEntity getUserByUsername(String userName) {
        // 这里我直接写死了，真实业务场景，到数据库去取
        UserEntity user = null;
        if (Objects.equals("admin", userName)) {
            user = new UserEntity();
            user.setUserName(userName);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setIsDeleted(0);
            user.setIsLocked(0);
            user.setStatus(0);
        }
        return user;
    }
}
