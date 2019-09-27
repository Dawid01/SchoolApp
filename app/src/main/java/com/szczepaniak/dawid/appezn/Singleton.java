package com.szczepaniak.dawid.appezn;

import android.app.Activity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.szczepaniak.dawid.appezn.Models.Comment;
import com.szczepaniak.dawid.appezn.Models.Post;
import com.szczepaniak.dawid.appezn.Models.User;

import java.util.ArrayList;
import java.util.List;

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

    private List<Comment> comments = new ArrayList<>();

    private List<GalleryImage> galleryImages = new ArrayList<>();

    private Post post;

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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<GalleryImage> getGalleryImages() {
        return galleryImages;
    }

    public void setGalleryImages(List<GalleryImage> galleryImages) {
        this.galleryImages = galleryImages;
    }
}
