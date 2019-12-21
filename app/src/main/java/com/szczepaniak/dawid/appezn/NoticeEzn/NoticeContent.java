package com.szczepaniak.dawid.appezn.NoticeEzn;

import com.google.gson.annotations.SerializedName;

public class NoticeContent {

    @SerializedName("rendered")
    private String rendered;

    public NoticeContent() {
    }

    public String getRendered() {
        return rendered;
    }
}
