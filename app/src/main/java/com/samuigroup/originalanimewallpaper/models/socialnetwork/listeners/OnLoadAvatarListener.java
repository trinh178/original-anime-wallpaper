package com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners;

import android.graphics.Bitmap;

public interface OnLoadAvatarListener {
    void onSuccess(Bitmap bmp);
    void onFailure(String msg);
}
