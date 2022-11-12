package com.itjing.neo4j.dao;

import com.itjing.neo4j.entity.Node;
import com.itjing.neo4j.entity.Relation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: lijing
 * @CreateTime: 2022-10-09 15:09
 */
@Repository
public interface RelationRepository extends Neo4jRepository<Relation, Long> {
    @Query("MATCH p=(n:Person)-[r:Relation]->(m:Person) " +
            "WHERE id(n)={startNode} and id(m)={endNode} and r.relation={relation}" +
            "RETURN p")
    List<Relation> findRelation(
            @Param("startNode") Node startNode,
            @Param("endNode") Node endNode,
            @Param("relation") String relation
    );
}