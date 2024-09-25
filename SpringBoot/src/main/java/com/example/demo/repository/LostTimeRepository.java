package com.example.demo.repository;

import com.example.demo.RelationDTO.LostTimeDTO;
import com.example.demo.relations.lost_time;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface LostTimeRepository extends Neo4jRepository<lost_time, Long> {

    @Query("MATCH (location:Location)-[r:lost_time]->(time:Time)\n" +
            "WHERE location IS NOT NULL AND time IS NOT NULL\n" +
            "RETURN location, time, r.count AS count")
    List<LostTimeDTO> findLostTime();
}

