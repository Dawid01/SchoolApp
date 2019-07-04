package com.szczepaniak.dawid.appezn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class Post {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("dateTime")
    @Expose
    private String dateTime;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("permission")
    @Expose
    private int permission;

    @SerializedName("userID")
    @Expose
    private long userID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
