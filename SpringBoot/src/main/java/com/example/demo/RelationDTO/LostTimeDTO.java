package com.example.demo.RelationDTO;

import com.example.demo.nodes.LocationEntity;
import com.example.demo.nodes.TimeEntity;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public class LostTimeDTO {
    private LocationEntity location;
    private TimeEntity time;
    private Integer count;

    public TimeEntity getTime() {
        return time;
    }

    public void setTime(TimeEntity time) {
        this.time = time;
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
