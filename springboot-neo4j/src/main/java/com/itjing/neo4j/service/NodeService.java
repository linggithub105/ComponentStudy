package com.itjing.neo4j.service;

import com.itjing.neo4j.entity.Node;

import java.util.List;

/**
 * @Description:
 * @Author: lijing
 * @CreateTime: 2022-10-08 16:52
 */
public interface NodeService {

    Node save(Node node);

    List<Node> getAll();

    void bind(String name1, String name2, String relationName);

}
