package com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners;

import com.samuigroup.originalanimewallpaper.models.entities.User;

public interface OnLoadUserListener {
    void onSuccess(User user);
    void onFailure(String msg);
}
