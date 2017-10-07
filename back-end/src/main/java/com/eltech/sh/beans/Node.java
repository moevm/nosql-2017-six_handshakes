package com.eltech.sh.beans;

public class Node {
    private int id;
    private String label;
    private String image;

    private Integer x;
    private Integer y;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Node(int id, String label, String image, Integer x, Integer y) {
        this.id = id;
        this.label = label;
        this.image = image;
        this.x = x;
        this.y = y;
    }

    //    public Node(int id, String label, String image) {
//        this.id = id;
//        this.label = label;
//        this.image = image;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
