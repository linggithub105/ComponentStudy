package com.itjing.security.service.impl;

import com.itjing.security.entity.RoleEntity;
import com.itjing.security.service.IRoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author lijing
 * @date 2022年06月09日 21:25
 * @description 角色业务实现类
 */
@Service
public class IRoleServiceImpl implements IRoleService {

    @Override
    public List<RoleEntity> getRoles(String userName) {
        // 这里我直接写死了，真实业务场景，到数据库去取
        List<RoleEntity> roleList = new ArrayList<>();
        RoleEntity role = new RoleEntity();
        role.setId(UUID.randomUUID().toString());
        role.setRoleName("ADMIN");
        roleList.add(role);
        return roleList;
    }
}
