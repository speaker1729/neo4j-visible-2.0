package com.example.demo.RelationDTO;

import com.example.demo.nodes.LocationEntity;
import com.example.demo.nodes.POIEntity;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public class LostPOIDTO {
    private LocationEntity location;
    private POIEntity POI;
    private Integer count;

    public POIEntity getPOI() {
        return POI;
    }

    public void setPOI(POIEntity POI) {
        this.POI = POI;
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
