package com.szczepaniak.dawid.appezn.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

public class FileUploaded{

    @SerializedName("filename")
    @Expose
    private String filename;

    @SerializedName("fileDownloadUri")
    @Expose
    private String fileDownloadUri;

    @SerializedName("fileType")
    @Expose
    private String fileType;

    @SerializedName("size")
    @Expose
    private String size;

    public FileUploaded() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
