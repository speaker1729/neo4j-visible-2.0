package com.example.demo.repository;

import com.example.demo.nodes.TimeEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeRepository extends Neo4jRepository<TimeEntity, Long> {

    /**
     * 根据时间名称查找时间实体，名称搜索不区分大小写。
     * 使用时间名称的小写形式来进行包含性比较，确保查询对大小写不敏感。
     *
     * @param timeName 要搜索的时间名称。
     * @return 匹配的时间实体列表。
     */
    @Query("MATCH (time:Time) " +
            "WHERE toLower(time.name) CONTAINS toLower(:#{#timeName}) " +
            "RETURN time")
    List<TimeEntity> findByTimeName(@Param("timeName") String timeName);

    /**
     * 查询所有时间实体。
     * 直接返回数据库中所有时间节点的列表。
     *
     * @return 所有时间实体的列表。
     */
    @Query("MATCH (time:Time) RETURN time")
    List<TimeEntity> findAllTime();
}
