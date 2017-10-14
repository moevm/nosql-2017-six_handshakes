package com.eltech.sh.beans;

import com.eltech.sh.enums.Nodes;

public class Node {
    private int id;
    private String label;
    private String image;
    private Nodes nodeType;

    public Node(int id, String label, String image, Nodes nodeType) {
        this.id = id;
        this.label = label;
        this.image = image;
        this.nodeType = nodeType;
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

    public Nodes getNodeType() {
        return nodeType;
    }

    public void setNodeType(Nodes nodeType) {
        this.nodeType = nodeType;
    }
}
