package com.example.demo.repository;

import com.example.demo.RelationDTO.LocatedInDTO;
import com.example.demo.relations.located_in;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface LocatedInRepository extends Neo4jRepository<located_in<?, ?>, Long> {

    @Query("MATCH (startNode)-[r:located_in]->(endNode) " +
            "WHERE (labels(startNode) = ['Area'] AND labels(endNode) = ['City']) OR " +
            "(labels(startNode) = ['City'] AND labels(endNode) = ['Province']) OR " +
            "(labels(startNode) = ['Area'] AND labels(endNode) = ['Province']) OR " +
            "(labels(startNode) = ['Location'] AND labels(endNode) = ['Province']) OR " +
            "(labels(startNode) = ['Location'] AND labels(endNode) = ['City']) OR " +
            "(labels(startNode) = ['Location'] AND labels(endNode) = ['Area']) " +
            "RETURN startNode AS startNode, endNode AS endNode, r.count AS count")
    List<LocatedInDTO> findLocatedIn();
}
