package com.itjing.neo4j.controller;

import com.itjing.neo4j.entity.Node;
import com.itjing.neo4j.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author: lijing
 * @CreateTime: 2022-10-08 16:55
 */
@RestController
@RequestMapping("/neo4j")
public class Neo4jController {

    @Autowired
    private NodeService nodeService;

    @PostMapping("/save")
    public Node save() {
        Node node = new Node();
        node.setName("海拉");
        node.setTitle("死亡女神");
        Node saveNode = nodeService.save(node);
        return saveNode;
    }

    @GetMapping("/getAll")
    public List<Node> getAll() {
        return nodeService.getAll();
    }

    @PostMapping("/bind")
    public String bind() {
        nodeService.bind("海拉", "索尔", "姐弟");
        return "ok";
    }


}
