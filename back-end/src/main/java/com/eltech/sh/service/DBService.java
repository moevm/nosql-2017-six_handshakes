package com.eltech.sh.service;

import com.eltech.sh.beans.Edge;
import com.eltech.sh.beans.GraphBean;
import javafx.util.Pair;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;

@Service
public class DBService {
    private final Session session;

    @Autowired
    public DBService(Session session) {
        this.session = session;
    }

    public void migrateToDB(Integer curUser) {
        session.run("CREATE CONSTRAINT ON (person:{curUser}) ASSERT person.vkId IS UNIQUE",
                parameters("curUser", curUser));

        //  session.run("CREATE CONSTRAINT ON (person:Person) ASSERT (person.vkId, person.owner) IS NODE KEY");

        session.run("LOAD CSV FROM 'http://localhost:8080/csv' AS line\n" +
                        "MERGE (from:{curUser} { vkId: toInt(line[0])})\n" +
                        "MERGE (to:{curUser} { vkId: toInt(line[1])})\n" +
                        "MERGE ((from)-[:FRIEND]-(to))",
                parameters("curUser", curUser));
    }

    public List<Integer> findPathByQuery(Integer from, Integer to, Integer curUser) {
        StatementResult result = session.run(
                "MATCH (" +
                        "from:{curUser} {vkId:{from}})," +
                        "(to:{curUser} {vkId:{to}}), " +
                        "path = shortestPath((from)-[:FRIEND*..5]-(to)) " +
                        "RETURN path",
                parameters("from", from, "to", to, "curUser", curUser));

        List<Integer> resultIds = new ArrayList<>();

        if (result.hasNext()) {
            result.next().get(0).asPath().nodes().forEach(node -> resultIds.add(node.get("vkId").asInt()));
        }
        return resultIds;
    }

    public Pair<List<Edge>, List<Integer>> findWebByQuery(Integer from, Integer to, Integer curUser) {
        StatementResult result = session.run(
                "MATCH (" +
                        "from:{curUser} {vkId:{from}})," +
                        "(to:{curUser} {vkId:{to}}), " +
                        "path = allShortestPaths((from)-[:FRIEND*..5]-(to)) " +
                        "RETURN path",
                parameters("from", from, "to", to, "curUser", curUser));

        List<Edge> relations = new ArrayList<>();
        Map<Long, Integer> nodes = new HashMap<>();

        while (result.hasNext()) {
            Value cur = result.next().get(0);
            cur.asPath().nodes().forEach(node -> nodes.put(node.id(), node.get("vkId").asInt()));
            cur.asPath().relationships().forEach(rel -> relations.add(new Edge(nodes.get(rel.startNodeId()), nodes.get(rel.endNodeId()))));
        }

        List<Integer> ids = new ArrayList<>();
        for (Long idx : nodes.keySet()) {
            ids.add(nodes.get(idx));
        }

        return new Pair(relations, ids);
    }

    public Integer countPeople(Integer user) {
        StatementResult result = session.run(
                "MATCH (person: {curUser} }) RETURN count (person)",
                parameters("curUser", user));

        return result.single().get(0).asInt();
    }

    public void deleteCluster(Integer user) {
        session.run("MATCH (" +
                        "person:{curUser})\n" +
                        "DETACH DELETE person",
                parameters("curUser", user));
    }
}