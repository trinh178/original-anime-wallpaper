package com.samuigroup.originalanimewallpaper.models;

import android.content.Context;

import com.samuigroup.originalanimewallpaper.models.imagestorage.ImageStorageAPI;
import com.samuigroup.originalanimewallpaper.models.imagestorage.KonachanWebservice;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.FirebaseService;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.SocialNetworkAPI;

public abstract class Model {
    public static ImageStorageAPI getImageStorageAPI(Context context) {
        return KonachanWebservice.getInstance(context);
    }
    public static SocialNetworkAPI getSocialNetworkAPI(Context context) {
        return FirebaseService.getInstance(context);
    }
}
