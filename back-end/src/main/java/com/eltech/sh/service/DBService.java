package com.eltech.sh.service;

import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

@Service
public class DBService {
    private final Session session;

    @Autowired
    public DBService(Session session) {
        this.session = session;
    }

    public void migrateToDB() {
        session.run("CREATE CONSTRAINT ON (person:Person) ASSERT person.vkId IS UNIQUE");

        session.run("LOAD CSV FROM 'http://localhost:8080/csv' AS line\n" +
                "MERGE (from:Person { vkId: toInt(line[0])})\n" +
                "MERGE (to:Person { vkId: toInt(line[1])})\n" +
                "MERGE ((from)-[:FRIEND]-(to))");
    }

    public List<Integer> findPathByQuery(Integer from, Integer to) {
        StatementResult result = session.run(
                "MATCH (from:Person {vkId:{from}}),(to:Person {vkId:{to}}), path = shortestPath((from)-[:FRIEND*..5]-(to)) RETURN path",
                parameters("from", from, "to", to));

        List<Integer> resultIds = new ArrayList<>();

        if (result.hasNext()) {
            result.next().get(0).asPath().nodes().forEach(node -> resultIds.add(node.get("vkId").asInt()));
        }
        return resultIds;
    }

    public List<Integer> findWebByQuery(Integer from, Integer to) {
        StatementResult result = session.run(
                "MATCH (from:Person {vkId:{from}}),(to:Person {vkId:{to}}), path = allShortestPaths((from)-[:FRIEND*..5]-(to)) RETURN path",
                parameters("from", from, "to", to));

        List<Integer> resultIds = new ArrayList<>();
//think about it
        if (result.hasNext()) {
            result.next().get(0).asPath().nodes().forEach(node -> resultIds.add(node.get("vkId").asInt()));
        }
        return resultIds;
    }

    public Integer countPeople(Integer owner) {
        StatementResult result = session.run(
                "MATCH (person) RETURN count (person)");

        return result.single().get(0).asInt();
    }
}