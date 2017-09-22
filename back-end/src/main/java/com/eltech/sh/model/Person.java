package com.eltech.sh.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    @JsonIgnore
    private Long id;

    @JsonProperty("id")
    private Integer vkId;

    private String firstName;
    private String lastName;
    private String photoUrl;

    public Person() {
    }

    public Person(Integer vkId, String firstName, String lastName) {
        this.vkId = vkId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getVkId() {
        return vkId;
    }

    public void setVkId(Integer vkId) {
        this.vkId = vkId;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("first_name")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("photoUrl")
    public String getPhotoUrl() {
        return photoUrl;
    }

    @JsonProperty("photo_400_orig")
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}