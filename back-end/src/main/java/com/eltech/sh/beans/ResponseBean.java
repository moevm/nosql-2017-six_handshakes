package com.eltech.sh.beans;

public class ResponseBean {
    private Graph graph;
    private TimeBean timeStat;
    private Integer pathLength;
    private Integer currentWeb;


    public ResponseBean(Graph graph, TimeBean timeStat, Integer pathLength, Integer currentWeb) {
        this.graph = graph;
        this.timeStat = timeStat;
        this.pathLength = pathLength;
        this.currentWeb = currentWeb;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public TimeBean getTimeStat() {
        return timeStat;
    }

    public void setTimeStat(TimeBean timeStat) {
        this.timeStat = timeStat;
    }

    public Integer getPathLength() {
        return pathLength;
    }

    public void setPathLength(Integer pathLength) {
        this.pathLength = pathLength;
    }

    public Integer getCurrentWeb() {
        return currentWeb;
    }

    public void setCurrentWeb(Integer currentWeb) {
        this.currentWeb = currentWeb;
    }
}
