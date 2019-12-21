package com.szczepaniak.dawid.appezn.NoticeEzn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoticePost {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("date")
    @Expose
    private String date;


    public NoticePost() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
