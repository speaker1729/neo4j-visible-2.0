package com.example.demo.nodes;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity(label = "TripMode")
public class TripModeEntity extends BaseEntity {

    public TripModeEntity() {
        super();
    }

    public TripModeEntity(Integer count, String name, Integer UUID) {
        super(count, name, UUID);
    }
}
