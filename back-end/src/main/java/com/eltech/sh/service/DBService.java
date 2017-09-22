package com.eltech.sh.service;

import com.eltech.sh.model.Person;
import org.neo4j.driver.v1.Record;
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
        session.run("LOAD CSV FROM 'file:///opa.csv' AS line\n" +
                "MERGE (from:Person { vkId: toInt(line[0])})\n" +
                "MERGE (to:Person { vkId: toInt(line[1])})\n" +
                "MERGE ((from)-[:FRIEND]-(to))");
    }

    List<Person> findPathByQuery(Integer from, Integer to) {
        StatementResult result = session.run(
                "MATCH (from:Person {vkId:{from}}),(to:Person {vkId:{to}}), path = shortestPath((from)-[:FRIEND*..5]-(to)) RETURN path",
                parameters("from", from, "to", to));

        while(result.hasNext()){
            Record record = result.next();
            System.out.println(record.get("vkId"));
        }
        return new ArrayList<>();
    }
}
//        session.run("LOAD CSV FROM 'file:///opa.csv' AS line\n" +
//                "MERGE (:Person { vkId: toInt(line[0]), firstName: line[1], lastName: line[2]})");


//        session.run("CREATE (a:Person {name: {name}, title: {title}})",
//                parameters("name", "Arthur", "title", "King"));
//
//        StatementResult result = session.run("MATCH (a:Person) WHERE a.name = {name} " +
//                        "RETURN a.name AS name, a.title AS title",
//                parameters("name", "Arthur"));
//        while (result.hasNext()) {
//            Record record = result.next();
//            System.out.println(record.get("title").asString() + " " + record.get("name").asString());
//        }