package com.samuigroup.originalanimewallpaper.models.entities;

import java.util.Date;

public class Comment {
    private String id;
    private String postId;
    private String username;
    private Date date;
    private String text;

    public Comment(String id, String postId, String username, Date date, String text) {
        this.id = id;
        this.postId = postId;
        this.username = username;
        this.date = date;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
