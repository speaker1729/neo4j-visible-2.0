package com.example.demo.repository;

import com.example.demo.nodes.POIEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface POIRepository extends Neo4jRepository<POIEntity, Long> {

    /**
     * 根据兴趣点(POI)名称搜索POI，名称搜索不区分大小写。
     * 使用POI名称的小写形式来进行包含性比较，确保查询对大小写不敏感。
     *
     * @param poiName 要搜索的POI名称。
     * @return 匹配的POI实体列表。
     */
    @Query("MATCH (poi:POI) " +
            "WHERE toLower(poi.name) CONTAINS toLower(:#{#poiName}) " +
            "RETURN poi")
    List<POIEntity> findByPOIName(@Param("poiName") String poiName);

    /**
     * 查询所有POI实体。
     * 直接返回数据库中所有POI节点的列表。
     *
     * @return 所有POI实体的列表。
     */
    @Query("MATCH (poi:POI) RETURN poi")
    List<POIEntity> findAllPOI();
}
