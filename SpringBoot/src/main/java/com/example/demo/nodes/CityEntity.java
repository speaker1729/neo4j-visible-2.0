package com.example.demo.nodes;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity(label = "City")
public class CityEntity extends BaseEntity {

    public CityEntity() {
        super();
    }

    public CityEntity(Integer count, String name, Integer UUID) {
        super(count, name, UUID);
    }
}
