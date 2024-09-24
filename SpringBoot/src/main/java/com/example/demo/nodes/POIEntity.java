package com.example.demo.nodes;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity(label = "POI")
public class POIEntity extends BaseEntity {

    public POIEntity() {
        super();
    }

    public POIEntity(Integer count, String name, Integer UUID) {
        super(count, name, UUID);
    }
}
