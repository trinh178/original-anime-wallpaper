package com.samuigroup.originalanimewallpaper;

public interface ChangePasswordContract {
    interface View {
        void showDialog(String text);
        void closeDialog();
        void clearInput();
        void showError(String text);
        void close();
    }
    interface Presenter {
        void onChange(String password, String newPassword, String cnewPassword);
        void onBack();
    }
}
