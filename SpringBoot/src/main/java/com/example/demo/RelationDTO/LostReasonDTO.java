package com.example.demo.RelationDTO;

import com.example.demo.nodes.LocationEntity;
import com.example.demo.nodes.ReasonEntity;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public class LostReasonDTO {
    private LocationEntity location;
    private ReasonEntity reason;
    private Integer count;

    public ReasonEntity getReason() {
        return reason;
    }

    public void setReason(ReasonEntity reason) {
        this.reason = reason;
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
