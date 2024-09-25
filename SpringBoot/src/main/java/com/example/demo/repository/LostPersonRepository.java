package com.example.demo.repository;

import com.example.demo.RelationDTO.LostPersonDTO;
import com.example.demo.relations.lost_person;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface LostPersonRepository extends Neo4jRepository<lost_person, Long> {

    @Query("MATCH (location:Location)-[r:lost_person]->(person:Person)\n" +
            "WHERE location IS NOT NULL AND person IS NOT NULL\n" +
            "RETURN location, person, r.count AS count")
    List<LostPersonDTO> findLostPerson();
}
