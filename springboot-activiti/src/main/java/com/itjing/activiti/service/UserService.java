package com.itjing.activiti.service;

import com.itjing.activiti.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @Description: 用户业务层
 * @Author: lijing
 * @CreateTime: 2022-09-09 15:20
 */
public interface UserService<T extends User> extends UserDetailsService {

}