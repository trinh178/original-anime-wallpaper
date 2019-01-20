package com.samuigroup.originalanimewallpaper;

import com.samuigroup.originalanimewallpaper.models.entities.Post;

public interface MyFavoritesContract {
    interface View {
        void showPageNumber(int offset, int count);
        void showPosts(Post[] posts);
        void showError(String msg);
        void close();
        void showDialog(String text);
        void closeDialog();
    }
    interface Presenter {
        void onStart();
        void onNextPage();
        void onPreviousPage();
        void onBack();
    }
}
