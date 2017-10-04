package com.eltech.sh.beans;

public class Node {
    private int id;
    private String label;
    private String circularImage;

    public Node(int id, String label, String circularImage) {
        this.id = id;
        this.label = label;
        this.circularImage = circularImage;
    }

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

    public String getCircularImage() {
        return circularImage;
    }

    public void setCircularImage(String circularImage) {
        this.circularImage = circularImage;
    }
}
