package com.example.demo.nodes;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity(label = "Person")
public class PersonEntity extends BaseEntity {

    public PersonEntity() {
        super();
    }

    public PersonEntity(Integer count, String name, Integer UUID) {
        super(count, name, UUID);
    }
}
