package com.example.demo.RelationDTO;

import com.example.demo.nodes.PersonEntity;
import com.example.demo.nodes.TripModeEntity;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public class ByDTO {
    private PersonEntity person;
    private TripModeEntity tripMode;
    private Integer count;

    public PersonEntity getPerson() {
        return person;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }

    public TripModeEntity getTripMode() {
        return tripMode;
    }

    public void setTripMode(TripModeEntity tripMode) {
        this.tripMode = tripMode;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
