package com.example.demo.nodes;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity(label = "Location")
public class LocationEntity extends BaseEntity {

    public LocationEntity() {
        super();
    }

    public LocationEntity(Integer count, String name, Integer UUID) {
        super(count, name, UUID);
    }
}
