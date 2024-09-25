package com.example.demo.RelationDTO;

import com.example.demo.nodes.LocationEntity;
import com.example.demo.nodes.WeatherEntity;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public class LostWeatherDTO {
    private LocationEntity location;
    private WeatherEntity weather;
    private Integer count;

    public WeatherEntity getWeather() {
        return weather;
    }

    public void setWeather(WeatherEntity weather) {
        this.weather = weather;
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
