package com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners;

import com.samuigroup.originalanimewallpaper.models.entities.Category;

public interface OnLoadCategoriesListener {
    void onSuccess(Category[] categories);
    void onFailure(String msg);
}
