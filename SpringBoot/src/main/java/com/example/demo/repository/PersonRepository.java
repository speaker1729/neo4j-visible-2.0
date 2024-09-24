package com.example.demo.repository;

import com.example.demo.nodes.PersonEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends Neo4jRepository<PersonEntity, Long> {

    /**
     * 根据人名搜索人物，名称搜索不区分大小写。
     * 使用人名的小写形式来进行包含性比较，确保查询对大小写不敏感。
     *
     * @param personName 要搜索的人名。
     * @return 匹配的人物实体列表。
     */
    @Query("MATCH (person:Person) " +
            "WHERE toLower(person.name) CONTAINS toLower(:#{#personName}) " +
            "RETURN person")
    List<PersonEntity> findByPersonName(@Param("personName") String personName);

    /**
     * 查询所有人物实体。
     * 直接返回数据库中所有人物节点的列表。
     *
     * @return 所有人物实体的列表。
     */
    @Query("MATCH (person:Person) RETURN person")
    List<PersonEntity> findAllPerson();
}

