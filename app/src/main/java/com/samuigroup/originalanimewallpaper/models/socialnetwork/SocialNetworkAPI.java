package com.samuigroup.originalanimewallpaper.models.socialnetwork;

import android.graphics.Bitmap;

import com.samuigroup.originalanimewallpaper.models.entities.User;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnChangeMyAvatarListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnChangeMyPasswordListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnCommentListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnFavoriteListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadAvatarListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadCategoriesListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadCommentsListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadFavoritesListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadUserListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnSigninListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnSignupListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnUnfavoriteListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnUpdateCommentsListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnUpdateFavoritesListener;

public interface SocialNetworkAPI {
    static final int SIGNUP_FAILURE_ERROR = 0;

    // Authentication
    void signup(String username, String password, OnSignupListener listener);
    void signin(String username, String password, OnSigninListener listener);
    void signout();
    boolean isSignin();
    User getMyUser();

    // User
    void changeMyAvatar(Bitmap bmp, OnChangeMyAvatarListener listener);
    void changeMyPassword(String oldPassword, String newPassword, OnChangeMyPasswordListener listener);
    void loadAvatar(String username, OnLoadAvatarListener listener);
    void loadUser(String username, OnLoadUserListener listener);

    // Post
    // Favorite
    void favorite(String postId, OnFavoriteListener listener);
    void unfavorite(String postId, OnUnfavoriteListener listener);
    void loadFavorites(String postId, OnLoadFavoritesListener listener);
    Listener addOnUpdateFavoritesListener(String postId, OnUpdateFavoritesListener listener);
    // Comment
    void comment(String postId, String text, OnCommentListener listener);
    void loadComments(String postId, OnLoadCommentsListener listener);
    Listener addOnUpdateCommentsListener(String postId, OnUpdateCommentsListener listener);

    // Other
    void loadCategories(OnLoadCategoriesListener listener);
}
