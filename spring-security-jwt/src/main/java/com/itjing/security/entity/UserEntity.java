package com.itjing.security.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lijing
 * @date 2022年06月09日 20:18
 * @description 用户实体类
 */
@Getter
@Setter
public class UserEntity {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 是否锁定
     */
    private Integer isLocked;

    /**
     * 激活状态
     */
    private Integer status;
}
