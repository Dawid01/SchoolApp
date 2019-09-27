package com.szczepaniak.dawid.appezn.Models;

public class Class {

    private Long id;

    private String externalID;

    private String name;

    public Class() {
    }

    public Class(Long id, String externalID, String name) {
        this.id = id;
        this.externalID = externalID;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
