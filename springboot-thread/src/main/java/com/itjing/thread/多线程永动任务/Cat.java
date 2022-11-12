package com.itjing.thread.多线程永动任务;

import lombok.Data;

/**
 * @Description:
 * @Author: lijing
 * @CreateTime: 2022-09-28 10:05
 */
@Data
public class Cat {
    private String catName;

    public Cat setCatName(String name) {
        this.catName = name;
        return this;
    }
}