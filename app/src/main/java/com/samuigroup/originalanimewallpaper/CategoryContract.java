package com.samuigroup.originalanimewallpaper;

import com.samuigroup.originalanimewallpaper.models.entities.Category;

public interface CategoryContract {
    interface View {
        void showCategories(Category[] categories);
        void showError(String text);
        void showDialog(String text);
        void closeDialog();
        void close();
    }
    interface Presenter {
        void onStart();
    }
}
