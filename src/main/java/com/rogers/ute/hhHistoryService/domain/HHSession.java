package com.rogers.ute.hhHistoryService.domain;

public class HHSession {
    private String startTime;
    private String endTime;
    private String sessionUsage;

    public HHSession(){}

    public HHSession(String startTime, String endTime, String sessionUsage) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionUsage = sessionUsage;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSessionUsage() {
        return sessionUsage;
    }

    public void setSessionUsage(String sessionUsage) {
        this.sessionUsage = sessionUsage;
    }
}
