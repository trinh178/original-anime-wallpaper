package com.samuigroup.originalanimewallpaper.models.imagestorage.listeners;

import com.samuigroup.originalanimewallpaper.models.entities.Post;

public interface OnLoadPostsListener {
    void onSuccessful(Post[] posts);
    void onFailure(String message);
}
