package com.example.demo.repository;

import com.example.demo.RelationDTO.LostPOIDTO;
import com.example.demo.relations.lost_POI;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface LostPOIRepository extends Neo4jRepository<lost_POI, Long> {
    @Query("MATCH (location:Location)-[r:lost_POI]->(poi:POI)\n" +
            "WHERE location IS NOT NULL AND poi IS NOT NULL\n" +
            "RETURN location, poi, r.count AS count")
    List<LostPOIDTO> findLostPOI();
}
