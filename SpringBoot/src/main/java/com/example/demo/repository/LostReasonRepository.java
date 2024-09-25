package com.example.demo.repository;

import com.example.demo.RelationDTO.LostReasonDTO;
import com.example.demo.relations.lost_reason;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface LostReasonRepository extends Neo4jRepository<lost_reason, Long> {

    @Query("MATCH (location:Location)-[r:lost_reason]->(reason:Reason)\n" +
            "WHERE location IS NOT NULL AND reason IS NOT NULL\n" +
            "RETURN location, reason, r.count AS count")
    List<LostReasonDTO> findLostReason();
}
