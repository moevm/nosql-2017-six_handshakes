package com.eltech.sh.beans;

public class Node {
    private int id;
    private String label;
    private String image;

    public Node(int id, String label, String image) {
        this.id = id;
        this.label = label;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
