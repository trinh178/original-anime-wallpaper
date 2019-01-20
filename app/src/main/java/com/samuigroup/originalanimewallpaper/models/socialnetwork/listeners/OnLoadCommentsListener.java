package com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners;

import com.samuigroup.originalanimewallpaper.models.entities.Comment;

public interface OnLoadCommentsListener {
    void onSuccess(Comment[] comments);
    void onFailure(String msg);
}
