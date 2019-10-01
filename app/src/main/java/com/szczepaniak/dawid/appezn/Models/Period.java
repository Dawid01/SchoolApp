package com.szczepaniak.dawid.appezn.Models;

public class Period {

    private Long id;

    private String externalID;

    private String period;

    private String startTime;

    private String endTime;

    public Period() {
    }

    public Period(String externalID, String period, String startTime, String endTime) {
        this.externalID = externalID;
        this.period = period;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalID() {
        return externalID;
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
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
}
