package com.eltech.sh.repository;


import com.eltech.sh.model.Person;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends GraphRepository<Person> {

    @Query("MATCH (fromId {vkId:{from}}) " +
            "MATCH (toId {vkId:{to}}) " +
            "MATCH path = shortestPath( (fromId)-[:FRIEND*..5]-(toId) ) " +
            "RETURN path")
    Iterable<Person> findPathByQuery(@Param("from") Integer from, @Param("to") Integer to);

    @Query("MATCH (:Person{vkId:{personId}})-->(friends) " +
            "RETURN friends")
    Iterable<Person> findFriendsById(@Param("personId") Integer id);

}