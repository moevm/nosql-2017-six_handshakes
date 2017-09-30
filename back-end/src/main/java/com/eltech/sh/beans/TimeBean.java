package com.eltech.sh.beans;

public class TimeBean {
    private Long dbTime;
    private Long vkTime;
    private Long pathTime;

    public TimeBean(Long dbTime, Long vkTime, Long pathTime) {
        this.dbTime = dbTime;
        this.vkTime = vkTime;
        this.pathTime = pathTime;
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
}
