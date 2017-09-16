package com.eltech.sh.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Index;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    @GraphId
    @JsonIgnore
    private Long id;

    @JsonProperty("id")
    @Index(unique=true,primary = true)
    private Long vkId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    public Person() {
    }

    public Person(Long vkId, String firstName, String lastName) {
        this.vkId = vkId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getVkId() {
        return vkId;
    }

    public void setVkId(Long vkId) {
        this.vkId = vkId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Neo4j doesn't REALLY have bi-directional relationships. It just means when querying
     * to ignore the direction of the relationship.
     * https://dzone.com/articles/modelling-data-neo4j
     */
    @Relationship(type = "FRIEND", direction = Relationship.UNDIRECTED)
    @JsonIgnore
    public Set<Person> friends;



    public void friendOf(Person person) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(person);
    }

//    public String toString() {
//
//        return this.name + "'s teammates => "
//                + Optional.ofNullable(this.teammates).orElse(
//                Collections.emptySet()).stream().map(
//                person -> person.getName()).collect(Collectors.toList());
//    }

}