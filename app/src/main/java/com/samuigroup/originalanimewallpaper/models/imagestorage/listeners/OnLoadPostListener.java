package com.samuigroup.originalanimewallpaper.models.imagestorage.listeners;

import com.samuigroup.originalanimewallpaper.models.entities.Post;

public interface OnLoadPostListener {
    void onSuccessful(Post post);
    void onFailure(String message);
}
