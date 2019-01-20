package com.samuigroup.originalanimewallpaper.models.entities;

import java.io.Serializable;

public class Post implements Serializable {
    private String id;
    private String author;
    private String previewUrl;
    private String sampleUrl;
    private String fileUrl;
    private String[] tags;

    public Post(String id, String author, String previewUrl, String sampleUrl, String fileUrl, String[] tags) {
        this.id = id;
        this.author = author;
        this.previewUrl = previewUrl;
        this.sampleUrl = sampleUrl;
        this.fileUrl = fileUrl;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getSampleUrl() {
        return sampleUrl;
    }

    public void setSampleUrl(String sampleUrl) {
        this.sampleUrl = sampleUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
