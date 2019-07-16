package com.szczepaniak.dawid.appezn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PostList {

    @SerializedName("content")
    @Expose
    List<Post> postList;

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }


}
