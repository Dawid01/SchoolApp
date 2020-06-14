package com.szczepaniak.dawid.appezn.Models;

public class ModelAR {

    private Long id;

    private String name;

    private String modelURL;

    private String imageURL;

    public ModelAR(Long id) {
        this.id = id;
    }

    public ModelAR(Long id, String name, String modelURL, String imageURL) {
        this.id = id;
        this.name = name;
        this.modelURL = modelURL;
        this.imageURL = imageURL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelURL() {
        return modelURL;
    }

    public void setModelURL(String modelURL) {
        this.modelURL = modelURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
