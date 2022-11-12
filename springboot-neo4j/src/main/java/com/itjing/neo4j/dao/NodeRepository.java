package com.itjing.neo4j.dao;

import com.itjing.neo4j.entity.Node;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 持久层
 * @Author: lijing
 * @CreateTime: 2022-10-08 16:50
 */
@Repository
public interface NodeRepository extends Neo4jRepository<Node, Long> {

    @Query("MATCH p=(n:Person) RETURN p")
    List<Node> selectAll();

    @Query("MATCH(p:Person{name:$name}) return p")
    Node findByName(@Param("name") String name);

}