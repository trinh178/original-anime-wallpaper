package com.samuigroup.originalanimewallpaper;

import android.graphics.Bitmap;

public interface ChangeAvatarContract {
    interface View {
        void showAvatar(Bitmap bmp);
        void showDialog(String text);
        void showError(String text);
        void closeDialog();
        void close();
    }
    interface Presenter {
        void onStart();
        void onChange(Bitmap bmp);
        void onBack();
    }
}
