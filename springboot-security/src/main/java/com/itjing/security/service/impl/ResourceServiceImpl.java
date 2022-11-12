package com.itjing.security.service.impl;

import com.itjing.security.entity.Resource;
import com.itjing.security.mapper.ResourceMapper;
import com.itjing.security.service.IResourceService;
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
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

}
