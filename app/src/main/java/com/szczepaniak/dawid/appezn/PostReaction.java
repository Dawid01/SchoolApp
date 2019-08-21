package com.szczepaniak.dawid.appezn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.http.POST;

public class PostReaction {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("reaction")
    @Expose
    private int reaction;

    @SerializedName("userID")
    @Expose
    private long userID;

    @SerializedName("post")
    @Expose
    private Post post;

    public PostReaction(int reaction, long userID) {
        this.reaction = reaction;
        this.userID = userID;
    }

    public PostReaction() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getReaction() {
        return reaction;
    }

    public void setReaction(int reaction) {
        this.reaction = reaction;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
