package com.example.demo.nodes;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity(label = "Area")
public class AreaEntity extends BaseEntity {

    public AreaEntity() {
        super();
    }

    public AreaEntity(Integer count, String name, Integer UUID) {
        super(count, name, UUID);
    }
}
