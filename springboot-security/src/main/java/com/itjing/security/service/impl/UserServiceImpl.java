package com.itjing.security.service.impl;

import com.itjing.security.entity.User;
import com.itjing.security.mapper.UserMapper;
import com.itjing.security.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lijing
 * @since 2022-08-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
