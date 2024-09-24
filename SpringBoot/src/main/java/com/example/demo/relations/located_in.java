package com.example.demo.relations;

import com.example.demo.nodes.BaseEntity;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

@Data
@RelationshipEntity(type = "located_in")
public class located_in<T extends BaseEntity, U extends BaseEntity> {

    @StartNode
    private T startNode;

    @EndNode
    private U endNode;

    @Property
    private Integer count;

    @Transient
    private String relation = "located_in";

    public located_in() {
    }

    public located_in(T startNode, U endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }
}
