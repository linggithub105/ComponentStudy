package com.itjing.neo4j.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

/**
 * @Description: 关联
 * @Author: lijing
 * @CreateTime: 2022-10-09 15:07
 */
@Data
@RelationshipEntity(type = "Relation")
public class Relation {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Node startNode;

    @EndNode
    private Node endNode;

    @Property
    private String relation;

}