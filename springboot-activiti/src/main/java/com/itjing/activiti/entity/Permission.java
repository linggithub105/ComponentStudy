package com.itjing.activiti.entity;

import lombok.Data;

/**
 * @Description: 权限类
 * @Author: lijing
 * @CreateTime: 2022-09-09 15:12
 */
@Data
public class Permission {

    private Integer id;

    private String url;

    private String name;

    private String description;

}