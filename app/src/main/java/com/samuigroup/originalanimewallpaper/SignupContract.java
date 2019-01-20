package com.samuigroup.originalanimewallpaper;

public interface SignupContract {
    interface View {
        void opemSigninView();
        void showDialog(String text);
        void closeDialog();
        void showError(String text);
        void close();
    }
    interface Presenter {
        void onSignup(String username, String password, String confirmPassword);

        void onBack();
    }
}
