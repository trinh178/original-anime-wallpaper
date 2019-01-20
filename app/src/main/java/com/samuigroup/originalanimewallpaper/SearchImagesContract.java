package com.samuigroup.originalanimewallpaper;

import com.samuigroup.originalanimewallpaper.models.entities.Post;

public interface SearchImagesContract {
    interface View extends BaseContract.View {
        void showPageNumber(int offset, int count);
        void showPosts(Post[] posts);
        void openPostView(Post post);
        void showDialog(String text);
        void closeDialog();
    }
    interface Presenter extends BaseContract.Presenter {
        void onStart(String tags[]);
        void onNextPage();
        void onPreviousPage();
    }
}
