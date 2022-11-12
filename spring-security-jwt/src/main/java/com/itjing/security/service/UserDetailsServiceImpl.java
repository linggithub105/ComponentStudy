package com.itjing.security.service;

import com.itjing.security.entity.JwtUserDetails;
import com.itjing.security.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author lijing
 * @date 2022年06月09日 20:56
 * @description
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity user = userService.getUserByUsername(userName);
        if (user == null) {
            throw new UsernameNotFoundException(userName);
        }
        return new JwtUserDetails(user, roleService, userService);
    }
}