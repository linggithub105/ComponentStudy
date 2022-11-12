package com.itjing.sharding.entity;

import lombok.Data;

/**
 * @author lijing
 * @date 2022年06月19日 10:45
 * @description
 */
@Data
public class User {
    private Integer id;

    private String nickname;

    private String password;

    private Integer sex;

    private String birthday;
}