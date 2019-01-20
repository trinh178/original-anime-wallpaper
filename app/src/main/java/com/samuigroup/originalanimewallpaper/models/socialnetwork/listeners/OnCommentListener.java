package com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners;

import com.samuigroup.originalanimewallpaper.models.entities.Comment;

public interface OnCommentListener {
    void onSuccess(Comment comment);
    void onFailure(String msg);
}
