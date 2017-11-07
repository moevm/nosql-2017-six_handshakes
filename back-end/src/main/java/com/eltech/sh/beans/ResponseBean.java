package com.eltech.sh.beans;

public class ResponseBean {
    private Graph graph;
    private TimeBean timeStat;
    private Integer pathLength;
    private Integer currentWeb;
    private String exportURL;


    public ResponseBean(Graph graph, TimeBean timeStat, Integer pathLength, Integer currentWeb, String exportURL) {
        this.graph = graph;
        this.timeStat = timeStat;
        this.pathLength = pathLength;
        this.currentWeb = currentWeb;
        this.exportURL = exportURL;
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

    public String getExportURL() {
        return exportURL;
    }

    public void setExportURL(String exportURL) {
        this.exportURL = exportURL;
    }
}
