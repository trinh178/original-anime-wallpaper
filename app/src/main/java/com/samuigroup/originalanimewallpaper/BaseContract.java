package com.samuigroup.originalanimewallpaper;

import android.graphics.Bitmap;

public interface BaseContract {
    interface View {
        void showTagsSuggestion(String[] tags);
        void setTagsText(String tags);
        void showAvatar(Bitmap bmp);
        void showUsername(String username);
        void setNavigationMenu(boolean isSignin);
        void close();

        // Navigation
        void openChangeAvatarView();
        void openChangePasswordView();
        void openMyFavoritesView();
        void openSigninView();
        void openSignupView();
        void openCategories();
    }
    interface Presenter {
        void onStart();
        void onTypeSearch(String name);
        void onSearch(String[] tags, int limit, int page);

        // Navigation
        void onNavClickChangeAvatar();
        void onNavClickChangePassword();
        void onNavClickImagesFavorite();
        void onNavClickSignout();
        void onNavClickExit();
        void onNavClickSignin();
        void onNavClickSignup();
        void onNavClickCategories();
    }
}
