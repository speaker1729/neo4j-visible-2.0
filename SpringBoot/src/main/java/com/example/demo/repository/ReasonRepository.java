package com.example.demo.repository;

import com.example.demo.nodes.ReasonEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReasonRepository extends Neo4jRepository<ReasonEntity, Long> {

    /**
     * 根据原因名称查询原因实体。
     * 使用原因名称进行精确匹配。
     *
     * @param reasonName 要搜索的原因名称。
     * @return 匹配的原因实体列表。
     */
    @Query("MATCH (reason:Reason) WHERE reason.name = :#{#reasonName} " +
            "RETURN reason")
    List<ReasonEntity> findByReasonName(@Param("reasonName") String reasonName);

    /**
     * 查询所有原因实体。
     * 直接返回数据库中所有原因节点的列表。
     *
     * @return 所有原因实体的列表。
     */
    @Query("MATCH (reason:Reason) RETURN reason")
    List<ReasonEntity> findAllReason();
}









