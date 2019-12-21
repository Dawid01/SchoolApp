package com.szczepaniak.dawid.appezn.NoticeEzn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoticePost {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("title")
    @Expose
    private NoticeTitle title;

    @SerializedName("content")
    @Expose
    private NoticeContent content;

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

    public NoticeTitle getTitle() {
        return title;
    }

    public void setTitle(NoticeTitle title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public NoticeContent getContent() {
        return content;
    }

    public void setContent(NoticeContent content) {
        this.content = content;
    }
}
