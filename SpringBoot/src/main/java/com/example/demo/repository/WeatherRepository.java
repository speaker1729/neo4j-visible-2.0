package com.example.demo.repository;

import com.example.demo.nodes.WeatherEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WeatherRepository extends Neo4jRepository<WeatherEntity, Long> {

    /**
     * 根据区域名称搜索区域，名称搜索不区分大小写。
     * 使用区域名称的小写形式来进行包含性比较，确保查询对大小写不敏感。
     *
     * @param WeatherName 要搜索的区域名称。
     * @return 匹配的区域实体列表。
     */
    @Query("MATCH (weather:Weather) " +
            "WHERE toLower(weather.name) CONTAINS toLower(:#{#WeatherName}) " +
            "RETURN weather")
    List<WeatherEntity> findByWeatherName(@Param("WeatherName") String WeatherName);

    /**
     * 查询所有区域实体。
     * 直接返回数据库中所有区域节点的列表。
     *
     * @return 所有区域实体的列表。
     */
    @Query("MATCH (weather:Weather) RETURN weather")
    List<WeatherEntity> findAllWeather();
}
