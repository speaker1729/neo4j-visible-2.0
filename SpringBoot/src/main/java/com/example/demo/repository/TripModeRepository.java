package com.example.demo.repository;

import com.example.demo.nodes.TripModeEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripModeRepository extends Neo4jRepository<TripModeEntity, Long> {

    /**
     * 根据出行模式名称搜索出行模式，名称搜索不区分大小写。
     * 使用出行模式名称的小写形式来进行包含性比较，确保查询对大小写不敏感。
     *
     * @param tripModeName 要搜索的出行模式名称。
     * @return 匹配的出行模式实体列表。
     */
    @Query("MATCH (tripMode:TripMode) " +
            "WHERE toLower(tripMode.name) CONTAINS toLower(:#{#tripModeName}) " +
            "RETURN tripMode")
    List<TripModeEntity> findByTripModeName(@Param("tripModeName") String tripModeName);

    /**
     * 查询所有出行模式实体。
     * 直接返回数据库中所有出行模式节点的列表。
     *
     * @return 所有出行模式实体的列表。
     */
    @Query("MATCH (tripMode:TripMode) RETURN tripMode")
    List<TripModeEntity> findAllTripMode();
}
