package com.itjing.springboot.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author lijing
 * @date 2022年07月03日 17:40
 * @description
 */
@Getter
@Setter
@Accessors(chain = true)
@Builder
@ToString
public class User {

    private Integer id;

    private String userName;

    private String password;

    private Integer age;

}
