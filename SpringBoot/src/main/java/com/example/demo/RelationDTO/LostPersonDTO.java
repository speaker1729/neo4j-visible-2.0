package com.example.demo.RelationDTO;

import com.example.demo.nodes.LocationEntity;
import com.example.demo.nodes.PersonEntity;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public class LostPersonDTO {
    private LocationEntity location;
    private PersonEntity person;
    private Integer count;

    public PersonEntity getPerson() {
        return person;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
