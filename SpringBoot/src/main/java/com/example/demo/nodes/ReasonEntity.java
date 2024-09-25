package com.example.demo.nodes;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity(label = "Reason")
public class ReasonEntity extends BaseEntity {

    public ReasonEntity() {
        super();
    }

    public ReasonEntity(Integer count, String name, Integer UUID) {
        super(count, name, UUID);
    }
}
