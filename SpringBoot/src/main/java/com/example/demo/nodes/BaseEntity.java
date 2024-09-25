package com.example.demo.nodes;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;

@Data
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Property("count")
    private Integer count;

    @Property("name")
    private String name;

    @Property("UUID")
    private Integer UUID;

    protected BaseEntity() {
    }

    protected BaseEntity(Integer count, String name, Integer UUID) {
        this.count = count;
        this.name = name;
        this.UUID = UUID;
    }
}
