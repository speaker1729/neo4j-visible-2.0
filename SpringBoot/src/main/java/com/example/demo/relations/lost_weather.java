package com.example.demo.relations;

import com.example.demo.nodes.LocationEntity;
import com.example.demo.nodes.ReasonEntity;
import lombok.Builder;
import lombok.Data;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@Data
@RelationshipEntity(type = "lost_weather")
@Builder
public class lost_weather {

    @StartNode
    private LocationEntity location;

    @EndNode
    private ReasonEntity weather;

    @Property
    private Integer count;

    private String relation;

    public String getRelation() {
        this.relation = "lost_weather";
        return this.relation;
    }
}
