package com.samuigroup.originalanimewallpaper.presenters;

import android.content.Context;

import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.SigninContract;
import com.samuigroup.originalanimewallpaper.models.Model;
import com.samuigroup.originalanimewallpaper.models.entities.User;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnSigninListener;

public class SigninPresenter implements SigninContract.Presenter {
    private SigninContract.View view;

    public SigninPresenter(SigninContract.View view) {
        this.view = view;
    }

    // Presenter
    @Override
    public void onClickSignupNotice() {

    }
    @Override
    public void onClickSignin(String username, String password) {
        if (username.length() < 6 || password.length() < 6) {
            view.showNotification(((Context) view).getResources().getString(R.string.password_too_short));
            return;
        }
        view.showWaitingDialog();
        Model.getSocialNetworkAPI((Context) view).signin(username, password,
                new OnSigninListener() {
                    @Override
                    public void onSuccess(User myUser) {
                        view.showNotification("Success");
                        view.destroyWaitingDialog();
                        view.switchToSearchImagesView();
                    }

                    @Override
                    public void onFailure(String msg) {
                        view.showNotification(msg);
                        view.destroyWaitingDialog();
                    }
                });
    }
    @Override
    public void onBack() {
        view.close();
    }
}
