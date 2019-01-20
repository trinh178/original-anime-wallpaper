package com.samuigroup.originalanimewallpaper.presenters;

import android.content.Context;

import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.SignupContract;
import com.samuigroup.originalanimewallpaper.models.Model;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnSignupListener;

public class SignupPresenter implements SignupContract.Presenter {
    private SignupContract.View view;

    public  SignupPresenter(SignupContract.View view) {
        this.view = view;
    }

    // Presenter
    @Override
    public void onSignup(String username, String password, String confirmPassword) {
        if (username.length() < 6 || password.length() < 6) {
            view.showError(((Context) view).getResources().getString(R.string.password_too_short));
            return;
        }
        view.showDialog("Signup..");
        if (password.equals(confirmPassword)) {
            Model.getSocialNetworkAPI((Context) view).signup(username, password, new OnSignupListener() {
                @Override
                public void onSuccess() {
                    view.closeDialog();
                    view.showError("Successful");
                    view.opemSigninView();
                    view.close();
                }
                @Override
                public void onFailure(String msg) {
                    view.closeDialog();
                    view.showError(msg);
                }
            });
        } else {
            view.closeDialog();
            view.showError(((Context)view).getResources().getString(R.string.cpassword_not_match));
        }
    }
    @Override
    public void onBack() {
        view.close();
    }
}
