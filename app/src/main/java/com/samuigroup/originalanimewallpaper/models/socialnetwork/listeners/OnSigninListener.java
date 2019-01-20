package com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners;

import com.samuigroup.originalanimewallpaper.models.entities.User;

public interface OnSigninListener {
    void onSuccess(User myUser);
    void onFailure(String msg);
}
