package com.szczepaniak.dawid.appezn.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ModelARList {
    @SerializedName("content")
    @Expose
    private List<ModelAR> modelsAR = new ArrayList<>();

    public List<ModelAR> getModelsAR() {
        return modelsAR;
    }

    public void setModelsAR(List<ModelAR> modelsAR) {
        this.modelsAR = modelsAR;
    }
}
