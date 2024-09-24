package com.example.demo.nodes;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity(label = "Time")
public class TimeEntity extends BaseEntity {

    public TimeEntity() {
        super();
    }

    public TimeEntity(Integer count, String name, Integer UUID) {
        super(count, name, UUID);
    }
}

