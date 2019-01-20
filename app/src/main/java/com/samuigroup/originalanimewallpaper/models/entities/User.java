package com.samuigroup.originalanimewallpaper.models.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String username;
    private ArrayList<String> favorites; // [favoriteId = postId] array
    //private String[] comments; // [commentId = postId___gen] array

    public User(String username, ArrayList<String> favorites) {
        this.username = username;
        this.favorites = favorites;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<String> favorites) {
        this.favorites = favorites;
    }
}
