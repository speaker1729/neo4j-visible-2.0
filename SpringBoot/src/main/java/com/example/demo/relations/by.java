package com.example.demo.relations;


import com.example.demo.nodes.PersonEntity;
import com.example.demo.nodes.TripModeEntity;
import lombok.Builder;
import lombok.Data;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@Data
@RelationshipEntity(type = "by")
@Builder
public class by {

    @StartNode
    private PersonEntity person;

    @EndNode
    private TripModeEntity tripMode;

    @Property
    private Integer count;

    private String relation;

    public String getRelation() {
        this.relation = "by";
        return this.relation;
    }
}
