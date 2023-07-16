package com.itjing.tree;

import java.util.List;

/**
 * @Description: 树节点父类，所有需要使用TreeUtils工具类形成树形结构等操作的节点都需要实现该接口
 * @Author: lijing
 * @CreateTime: 2022-11-15 15:16
 *
 * 泛型说明：
 *
 * T 主要定义返回值的类型
 *
 * RC(rootCondition) 主要是定义根节点 (也就是父节点) 的自定义判定规则需要用到的参数类型
 *
 * LC(leafCondition) 主要是定义叶子结点（也就是子节点）的自定义判定规则需要用到的参数类型
 *
 */
public interface TreeNode<T, RC, LC> {

    /**
     * 获取树结点id
     *
     * @return
     */
    T getTreeNodeId();

    /**
     * 获取该节点的父节点id
     *
     * @return
     */
    T getParentId();

    /**
     * 判断该节点是否为根节点，默认判定
     *
     * @return
     * @Des 可以用于简单树的组件
     */
    boolean isRoot();

    /**
     * 自定义父结点的判定规则
     *
     * @param rootCondition
     * @return
     */
    boolean isRoot(RC rootCondition);

    /**
     * 自定义子节点(叶子结点)的判定规则
     *
     * @param leafCondition
     * @return
     */
    boolean isChildren(LC leafCondition);

    /**
     * 判断是否有子节点
     *
     * @return
     */
    boolean hasChild();

    /**
     * 设置结点的子节点列表
     *
     * @param children
     */
    void setChildren(List<? extends TreeNode<T, RC, LC>> children);

    /**
     * 获取所有子节点
     *
     * @return
     */
    List<? extends TreeNode<T, RC, LC>> getChildren();

    /**
     * 获取树的深度
     *
     * @return
     */
    Integer getLevel();

    /**
     * 设置树的深度
     */
    void setLevel(Integer level);

}