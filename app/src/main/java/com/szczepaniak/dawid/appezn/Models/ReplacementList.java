package com.szczepaniak.dawid.appezn.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReplacementList {

    @SerializedName("content")
    @Expose
    List<Replacement> replacementList;

    public List<Replacement> getReplacementList() {
        return replacementList;
    }

    public void setReplacementList(List<Replacement> replacementList) {
        this.replacementList = replacementList;
    }
}
