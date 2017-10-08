package com.eltech.sh.beans;

import com.eltech.sh.model.Person;

import java.util.List;

public class ResponseBean {
    private List<Person> people;
    private GraphBean graph;
    private TimeBean timeStat;
    private Integer peopleChecked;
    private Integer currentWeb;


    public ResponseBean(List<Person> people, GraphBean graph, TimeBean timeStat, Integer peopleChecked, Integer currentWeb) {
        this.people = people;
        this.graph = graph;
        this.timeStat = timeStat;
        this.peopleChecked = peopleChecked;
        this.currentWeb = currentWeb;
    }


    public GraphBean getGraph() {
        return graph;
    }

    public void setGraph(GraphBean graph) {
        this.graph = graph;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public TimeBean getTimeStat() {
        return timeStat;
    }

    public void setTimeStat(TimeBean timeStat) {
        this.timeStat = timeStat;
    }

    public Integer getPeopleChecked() {
        return peopleChecked;
    }

    public void setPeopleChecked(Integer peopleChecked) {
        this.peopleChecked = peopleChecked;
    }

    public Integer getCurrentWeb() {
        return currentWeb;
    }

    public void setCurrentWeb(Integer currentWeb) {
        this.currentWeb = currentWeb;
    }
}
