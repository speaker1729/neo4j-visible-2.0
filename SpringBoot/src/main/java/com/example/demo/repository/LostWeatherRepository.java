package com.example.demo.repository;

import com.example.demo.RelationDTO.LostWeatherDTO;
import com.example.demo.relations.lost_weather;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface LostWeatherRepository extends Neo4jRepository<lost_weather, Long> {

    @Query("MATCH (location:Location)-[r:lost_weather]->(weather:Weather)\n" +
            "WHERE location IS NOT NULL AND weather IS NOT NULL\n" +
            "RETURN location, weather, r.count AS count")
    List<LostWeatherDTO> findLostWeather();
}