package com.samuigroup.originalanimewallpaper;

public interface SigninContract {
    interface View {
        void showNotification(String text);
        void showWaitingDialog();
        void destroyWaitingDialog();
        void switchToSearchImagesView();
        void close();
    }
    interface Presenter {
        void onClickSignupNotice();
        void onClickSignin(String username, String password);
        void onBack();
    }
}
