package com.example.demo.repository;

import com.example.demo.RelationDTO.ByDTO;
import com.example.demo.relations.by;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface ByRepository extends Neo4jRepository<by, Long> {

    @Query("MATCH (person:Person)-[r:by]->(tripMode:TripMode)\n" +
            "WHERE person IS NOT NULL AND tripMode IS NOT NULL\n" +
            "RETURN person, tripMode, r.count AS count")
    List<ByDTO> findBY();
}
