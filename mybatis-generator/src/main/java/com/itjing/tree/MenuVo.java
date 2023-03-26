package com.itjing.tree;

import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @Description:
 * @Author: lijing
 * @CreateTime: 2022-11-15 15:32
 */
@Data
public class MenuVo implements TreeNode<Integer, Boolean, Integer> {

    private Integer id;

    private String name;

    private Integer parentId;

    private Integer level;

    private List<MenuVo> children;

    public MenuVo(Integer id, String name, Integer parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }


    @Override
    public Integer getTreeNodeId() {
        return this.id;
    }

    @Override
    public Integer getParentId() {
        return this.parentId;
    }

    @Override
    public boolean isRoot() {
        // 默认判定
        return Objects.isNull(this.parentId);
    }


    @Override
    public boolean isRoot(Boolean rootCondition) {
        // 自定义的父节点判定规则
        if (rootCondition) {
            return Objects.equals(this.id, 14);

        } else {
            // 都不符合就走默认判定条件
            return isRoot();
        }
    }

    @Override
    public boolean isChildren(Integer leafCondition) {
        // 自定义结点判定规则
        // 这里自定义规则当传入的参数等于1的时候 ——> 要销售部，只要销售部的带有“a”的部门名作为子树节点
        if (leafCondition.equals(1)) {
            if (this.name.contains("a")) {
                return true;
            } else {
                return false;
            }
        } else {
            // 都不符合就表示该节点不是自定义规则中要的结点
            return false;
        }

    }

    @Override
    public boolean hasChild() {
        return Objects.nonNull(this.children);
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }

    @Override
    public List<MenuVo> getChildren() {
        return this.children;
    }

}