package com.szczepaniak.dawid.appezn.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostReactionList {

    @SerializedName("content")
    @Expose
    List<PostReaction> postReactionList;

    public List<PostReaction> getPostReactionList() {
        return postReactionList;
    }

    public void setPostReactionList(List<PostReaction> postReactionList) {
        this.postReactionList = postReactionList;
    }
}
