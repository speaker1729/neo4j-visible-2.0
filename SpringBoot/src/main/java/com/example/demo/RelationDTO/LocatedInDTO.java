package com.example.demo.RelationDTO;

import com.example.demo.nodes.BaseEntity;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public class LocatedInDTO {
    private BaseEntity startNode;
    private BaseEntity endNode;
    private Integer count;

    public BaseEntity getStartNode() {
        return startNode;
    }

    public void setStartNode(BaseEntity startNode) {
        this.startNode = startNode;
    }

    public BaseEntity getEndNode() {
        return endNode;
    }

    public void setEndNode(BaseEntity endNode) {
        this.endNode = endNode;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
