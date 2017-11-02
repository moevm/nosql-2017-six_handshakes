package com.eltech.sh.beans;

public class ResponseBean {
    private GraphBean graph;
    private TimeBean timeStat;
    private Integer pathLength;
    private Integer currentWeb;
    private String exportURL;


    public ResponseBean(GraphBean graph, TimeBean timeStat, Integer pathLength, Integer currentWeb, String exportURL) {
        this.graph = graph;
        this.timeStat = timeStat;
        this.pathLength = pathLength;
        this.currentWeb = currentWeb;
        this.exportURL = exportURL;
    }

    public GraphBean getGraph() {
        return graph;
    }

    public void setGraph(GraphBean graph) {
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
