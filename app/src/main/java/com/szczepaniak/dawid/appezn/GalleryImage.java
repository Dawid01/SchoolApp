package com.szczepaniak.dawid.appezn;

public class GalleryImage {

    private String url;
    private boolean used;

    public GalleryImage(String url, boolean used) {
        this.url = url;
        this.used = used;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
