package com.itjing.sensitive.entity;

import com.itjing.sensitive.annotation.Sensitive;
import com.itjing.sensitive.enums.SensitiveEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 用户实体类
 * @Author: lijing
 * @CreateTime: 2022-11-03 10:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 姓名
     */
    @Sensitive(mask = SensitiveEnum.ALL_MASK)
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    @Sensitive(mask = SensitiveEnum.ALL_MASK, maskChar = "*")
    private String email;

}
