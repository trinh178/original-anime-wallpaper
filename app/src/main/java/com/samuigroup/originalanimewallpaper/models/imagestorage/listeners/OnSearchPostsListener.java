package com.samuigroup.originalanimewallpaper.models.imagestorage.listeners;

import com.samuigroup.originalanimewallpaper.models.entities.Post;

public interface OnSearchPostsListener {
    void onSuccessful(Post[] posts, int limit, int page, int count);
    void onFailure(String message);
}
