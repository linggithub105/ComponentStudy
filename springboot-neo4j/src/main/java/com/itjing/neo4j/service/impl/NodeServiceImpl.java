package com.itjing.neo4j.service.impl;

import com.itjing.neo4j.dao.NodeRepository;
import com.itjing.neo4j.dao.RelationRepository;
import com.itjing.neo4j.entity.Node;
import com.itjing.neo4j.entity.Relation;
import com.itjing.neo4j.service.NodeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: lijing
 * @CreateTime: 2022-10-08 16:51
 */
@Service
@AllArgsConstructor
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepository;

    private final RelationRepository relationRepository;

    @Override
    public Node save(Node node) {
        Node saveNode = nodeRepository.save(node);
        return saveNode;
    }

    @Override
    public List<Node> getAll() {
        return nodeRepository.selectAll();
    }

    @Override
    public void bind(String name1, String name2, String relationName) {
        Node start = nodeRepository.findByName(name1);
        Node end = nodeRepository.findByName(name2);
        Relation relation = new Relation();
        relation.setStartNode(start);
        relation.setEndNode(end);
        relation.setRelation(relationName);
        relationRepository.save(relation);
    }

}
