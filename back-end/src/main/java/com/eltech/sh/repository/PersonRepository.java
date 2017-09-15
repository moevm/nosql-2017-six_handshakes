package com.eltech.sh.repository;


import com.eltech.sh.model.Person;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface PersonRepository extends GraphRepository<Person> {

}