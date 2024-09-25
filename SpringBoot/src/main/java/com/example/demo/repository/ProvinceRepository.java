package com.example.demo.repository;

import com.example.demo.nodes.ProvinceEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProvinceRepository extends Neo4jRepository<ProvinceEntity, Long> {

    /**
     * 根据省份名称搜索省份，名称搜索不区分大小写。
     * 使用省份名称的小写形式来进行包含性比较，确保查询对大小写不敏感。
     *
     * @param provinceName 要搜索的省份名称。
     * @return 匹配的省份实体列表。
     */
    @Query("MATCH (province:Province) " +
            "WHERE toLower(province.name) CONTAINS toLower(:#{#provinceName}) " +
            "RETURN province")
    List<ProvinceEntity> findByProvinceName(@Param("provinceName") String provinceName);

    /**
     * 查询所有省份实体。
     * 直接返回数据库中所有省份节点的列表。
     *
     * @return 所有省份实体的列表。
     */
    @Query("MATCH (province:Province) RETURN province")
    List<ProvinceEntity> findAllProvince();
}
