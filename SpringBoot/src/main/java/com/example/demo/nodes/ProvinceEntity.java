package com.example.demo.nodes;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity(label = "Province")
public class ProvinceEntity extends BaseEntity {

    public ProvinceEntity() {
        super();
    }

    public ProvinceEntity(Integer count, String name, Integer UUID) {
        super(count, name, UUID);
    }
}
