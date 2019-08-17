package com.szczepaniak.dawid.appezn;

import android.app.Activity;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;

public class Singleton {
    private static final Singleton ourInstance = new Singleton();

    private long currentUserID;

    public static Singleton getInstance() {
        return ourInstance;
    }

    private User currentUser;

    private ImageLoader imageLoader;

    private String[] photos;

    private Activity mainActivity;

    private Singleton() {
    }

    public static Singleton getOurInstance() {
        return ourInstance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public long getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(long currentUserID) {
        this.currentUserID = currentUserID;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public String[] getPhotos() {
        return photos;
    }

    public void setPhotos(String[] photos) {
        this.photos = photos;
    }

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
