package com.eltech.sh.beans;

public class TimeBean {
    private Long dbTime;
    private Long vkTime;
    private Long pathTime;
    private Long csvTime;

    public TimeBean(Long dbTime, Long vkTime, Long pathTime, Long csvTime) {
        this.dbTime = dbTime;
        this.vkTime = vkTime;
        this.pathTime = pathTime;
        this.csvTime = csvTime;
    }

    public Long getDbTime() {
        return dbTime;
    }

    public void setDbTime(Long dbTime) {
        this.dbTime = dbTime;
    }

    public Long getVkTime() {
        return vkTime;
    }

    public void setVkTime(Long vkTime) {
        this.vkTime = vkTime;
    }

    public Long getPathTime() {
        return pathTime;
    }

    public void setPathTime(Long pathTime) {
        this.pathTime = pathTime;
    }

    public Long getCsvTime() {
        return csvTime;
    }

    public void setCsvTime(Long csvTime) {
        this.csvTime = csvTime;
    }
}
