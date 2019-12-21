package com.szczepaniak.dawid.appezn.NoticeEzn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NoticePostList {


    ArrayList<NoticePost> noticePosts;

    public ArrayList<NoticePost> getNoticePosts() {
        return noticePosts;
    }

    public void setNoticePosts(ArrayList<NoticePost> noticePosts) {
        this.noticePosts = noticePosts;
    }
}
