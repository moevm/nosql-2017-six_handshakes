package com.eltech.sh.service;

import com.eltech.sh.beans.Edge;
import javafx.util.Pair;
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
public class DBServiceImpl implements DBService {
    private final Session session;

    @org.springframework.beans.factory.annotation.Value("${app.url}")
    private String url;

    @Autowired
    public DBServiceImpl(Session session) {
        this.session = session;
    }

    @Override
    public void migrateToDB(Integer curUser) {
        session.run("CREATE CONSTRAINT ON (person:Person) ASSERT person.constraintId IS UNIQUE");

        session.run("LOAD CSV FROM '" + url + "/csv/'+{curUser} AS line\n" +
                        "MERGE (from:Person { vkId: toInt(line[0]), " +
                        "constraintId: toString(line[0]) + toString({curUser}), " +
                        "cluster:{curUser}})\n" +
                        "MERGE (to:Person { vkId: toInt(line[1]), " +
                        "constraintId: toString(line[1])+ toString({curUser}), " +
                        "cluster:{curUser}})\n" +
                        "MERGE ((from)-[:FRIEND]-(to))",
                parameters("curUser", curUser));
    }

    @Override
    public List<Integer> findPathByQuery(Integer from, Integer to, Integer curUser) {

        StatementResult result = session.run(
                "MATCH (" +
                        "from:Person {constraintId:toString({from}) + toString({curUser})})," +
                        "(to:Person {constraintId:toString({to}) + toString({curUser})}), " +
                        "path = shortestPath((from)-[:FRIEND*..5]-(to)) " +
                        "RETURN path",
                parameters("from", from, "to", to, "curUser", curUser));

        List<Integer> resultIds = new ArrayList<>();

        if (result.hasNext()) {
            result.next().get(0).asPath().nodes().forEach(node -> resultIds.add(node.get("vkId").asInt()));
        }
        return resultIds;
    }

    @Override
    public Pair<List<Edge>, List<Integer>> findWebByQuery(Integer from, Integer to, Integer curUser) {
        StatementResult result = session.run(
                "MATCH (" +
                        "from:Person {constraintId:toString({from}) + toString({curUser})})," +
                        "(to:Person {constraintId:toString({to}) + toString({curUser})}), " +
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

        return new Pair<>(relations, ids);
    }

    @Override
    public Integer countPeople(Integer user) {
        StatementResult result = session.run(
                "MATCH (person: Person) where person.cluster={curUser} RETURN count (person)",
                parameters("curUser", user));

        return result.single().get(0).asInt();
    }

    @Override
    public void deleteCluster(Integer user) {
        session.run("MATCH (person:Person) where person.cluster={curUser} DETACH DELETE person",
                parameters("curUser", user));
    }

    @Override
    public Boolean isPathExist(Integer from, Integer to, Integer curUser) {
        //FIXME execute direct query instead of it
        return findPathByQuery(from, to ,curUser).size() > 0;
    }
}