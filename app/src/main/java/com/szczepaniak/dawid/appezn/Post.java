package com.szczepaniak.dawid.appezn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.List;

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

    @SerializedName("photos")
    @Expose
    private String[] photos;

    @SerializedName("postReactions")
    @Expose
    private List<PostReaction> postReactions;


    public Post(Long id, String content, String dateTime, User user, int permission, long userID, String[] photos) {
        this.id = id;
        this.content = content;
        this.dateTime = dateTime;
        this.user = user;
        this.permission = permission;
        this.userID = userID;
        this.photos = photos;
    }

    public Post() {
    }

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

    public String[] getPhotos() {
        return photos;
    }

    public void setPhotos(String[] photos) {
        this.photos = photos;
    }

    public List<PostReaction> getPostReactions() {
        return postReactions;
    }

    public void setPostReactions(List<PostReaction> postReactions) {
        this.postReactions = postReactions;
    }
}
