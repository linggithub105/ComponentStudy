package com.itjing.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.itjing.excel.annotation.ExcelValid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 用户实体类
 * @Author: lijing
 * @CreateTime: 2022-09-07 14:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;

    @ExcelProperty(value = "用户名")
    @ExcelValid(message = "用户名称必填！")
    private String username;

    @ExcelProperty(value = "手机号码")
    @ExcelValid(message = "手机号码必填！")
    private String phone;
}
