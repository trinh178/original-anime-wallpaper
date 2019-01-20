package com.samuigroup.originalanimewallpaper.models.imagestorage;

import com.samuigroup.originalanimewallpaper.models.imagestorage.listeners.OnLoadPostListener;
import com.samuigroup.originalanimewallpaper.models.imagestorage.listeners.OnLoadPostsListener;
import com.samuigroup.originalanimewallpaper.models.imagestorage.listeners.OnSearchPostsListener;
import com.samuigroup.originalanimewallpaper.models.imagestorage.listeners.OnSearchTagsListener;

public interface ImageStorageAPI {
    // Post - Image
    void loadPost(String postId, OnLoadPostListener listener);
    void loadPosts(String[] postIds, OnLoadPostsListener listener);
    void searchPosts(String[] hashtags, int limit, int page, OnSearchPostsListener listener);

    // Tag
    void searchTags(String name, int limit, OnSearchTagsListener listener);
}
