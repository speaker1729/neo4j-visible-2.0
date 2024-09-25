package com.example.demo.repository;

import com.example.demo.nodes.CityEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends Neo4jRepository<CityEntity, Long> {

    /**
     * 根据城市名称搜索城市，名称搜索不区分大小写。
     * 使用城市名称的小写形式来进行包含性比较，确保查询对大小写不敏感。
     *
     * @param cityName 要搜索的城市名称。
     * @return 匹配的城市实体列表。
     */
    @Query("MATCH (city:City) " +
            "WHERE toLower(city.name) CONTAINS toLower(:#{#cityName}) " +
            "RETURN city")
    List<CityEntity> findByCityName(@Param("cityName") String cityName);

    /**
     * 查询所有城市实体。
     * 直接返回数据库中所有城市节点的列表。
     *
     * @return 所有城市实体的列表。
     */
    @Query("MATCH (city:City) RETURN city")
    List<CityEntity> findAllCity();
}
