package com.szczepaniak.dawid.appezn.NoticeEzn;

import com.google.gson.annotations.SerializedName;

public class NoticeTitle {

    @SerializedName("rendered")
    private String rendered;

    public NoticeTitle() {
    }

    public String getRendered() {
        return rendered;
    }
}
