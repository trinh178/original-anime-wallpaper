package com.samuigroup.originalanimewallpaper.models.imagestorage.listeners;

public interface OnSearchTagsListener {
    void onSuccessful(String[] tags);
    void onFailure(String message);
}
