package com.example.demo.nodes;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity(label = "Weather")
public class WeatherEntity extends BaseEntity {

    public WeatherEntity() {
        super();
    }

    public WeatherEntity(Integer count, String name, Integer UUID) {
        super(count, name, UUID);
    }
}
