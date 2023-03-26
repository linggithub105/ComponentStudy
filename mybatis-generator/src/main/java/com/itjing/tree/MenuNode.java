package com.itjing.tree;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: lijing
 * @CreateTime: 2022-11-15 16:35
 */
@Data
public class MenuNode {

    private Integer id;

    private String name;

    private Integer parentId;

    private Integer weight;

    private List<MenuNode> children;

    public MenuNode(Integer id, String name, Integer parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }


}
