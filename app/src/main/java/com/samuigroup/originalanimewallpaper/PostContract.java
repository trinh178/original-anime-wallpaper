package com.samuigroup.originalanimewallpaper;

import android.graphics.Bitmap;

import com.samuigroup.originalanimewallpaper.models.entities.Comment;
import com.samuigroup.originalanimewallpaper.models.entities.Post;

public interface PostContract {
    interface View {
        void openSearchImagesView(String[] tags);
        void showAdditionalComment(Comment comment, Bitmap avatar);

        void showFavoriteIcon(int favoriteStatus); // 0: unfavorite, 1: favorite, 2: processing
        void showFavoriteCount(int count);

        void showZoom(boolean show);

        void clearInput();
        void showDialog(String text);
        void closeDialog();
        void focusLatestComment();

        void showError(String text);
        void close();
    }
    interface Presenter {
        void onStart(Post post);
        void onTagClick(String tag);
        void onComment(String text);
        void onFavoriteClick();
        void onZoom();
        void onBack();
    }
}
