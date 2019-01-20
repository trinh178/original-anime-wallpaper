package com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners;

public interface OnLoadFavoritesListener {
    void onSuccess(String[] users);
    void onFailure(String msg);
}
