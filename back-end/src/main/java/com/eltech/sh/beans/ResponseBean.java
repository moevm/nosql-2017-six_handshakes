package com.eltech.sh.beans;

import com.eltech.sh.model.Person;

import java.util.List;

public class ResponseBean {
    private List<Person> people;
    private List<List<Person>> alternative;
    private TimeBean timeStat;
    private Integer peopleChecked;
    private Integer currentWeb;

    public ResponseBean(List<Person> people, List<List<Person>> alternative, TimeBean timeStat, Integer peopleChecked, Integer currentWeb) {
        this.people = people;
        this.alternative = alternative;
        this.timeStat = timeStat;
        this.peopleChecked = peopleChecked;
        this.currentWeb = currentWeb;
    }

    public List<List<Person>> getAlternative() {
        return alternative;
    }

    public void setAlternative(List<List<Person>> alternative) {
        this.alternative = alternative;
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
