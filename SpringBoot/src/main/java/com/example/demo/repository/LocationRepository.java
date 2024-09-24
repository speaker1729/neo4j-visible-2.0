package com.example.demo.repository;

import com.example.demo.nodes.LocationEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends Neo4jRepository<LocationEntity, Long> {

    /**
     * 根据地点名称搜索地点，名称搜索不区分大小写。
     * 使用地点名称的小写形式来进行包含性比较，确保查询对大小写不敏感。
     *
     * @param locationName 要搜索的地点名称。
     * @return 匹配的地点实体列表。
     */
    @Query("MATCH (location:Location) " +
            "WHERE toLower(location.name) CONTAINS toLower(:#{#locationName}) " +
            "RETURN location")
    List<LocationEntity> findByLocationName(@Param("locationName") String locationName);

    /**
     * 查询所有地点实体。
     * 直接返回数据库中所有地点节点的列表。
     *
     * @return 所有地点实体的列表。
     */
    @Query("MATCH (location:Location) RETURN location")
    List<LocationEntity> findAllLocation();


    /**
     * 根据省份名称查询与该省份相关的地点。
     *
     * @param provinceName 要搜索的省份名称
     * @return 与省份相关的地点实体列表
     */
    @Query("MATCH (l:Location)-[:located_in]-(province:Province) " +
            "WHERE toLower(province.name) CONTAINS toLower(:#{#provinceName}) " +
            "RETURN l")
    List<LocationEntity> findLocationByProvince(@Param("provinceName") String provinceName);

    /**
     * 根据城市名称查询与该城市相关的地点。
     *
     * @param cityName 要搜索的城市名称
     * @return 与城市名称相关的地点实体列表
     */
    @Query("MATCH (l:Location)-[:located_in]-(city:City) " +
            "WHERE toLower(city.name) CONTAINS toLower(:#{#cityName}) " +
            "RETURN l")
    List<LocationEntity> findLocationByCity(@Param("cityName") String cityName);

    /**
     * 根据地区名称查询与该地区相关的地点。
     *
     * @param areaName 要搜索的地区名称
     * @return 与地区相关的地点实体列表
     */
    @Query("MATCH (l:Location)-[:located_in]-(area:Area) " +
            "WHERE toLower(area.name) CONTAINS toLower(:#{#areaName}) " +
            "RETURN l")
    List<LocationEntity> findLocationByArea(@Param("areaName") String areaName);
}
