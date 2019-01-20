package com.samuigroup.originalanimewallpaper.presenters;

import android.content.Context;

import com.samuigroup.originalanimewallpaper.ChangePasswordContract;
import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.models.Model;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.SocialNetworkAPI;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnChangeMyPasswordListener;

public class ChangePasswordPresenter implements ChangePasswordContract.Presenter {
    private ChangePasswordContract.View view;

    public ChangePasswordPresenter(ChangePasswordContract.View view) {
        this.view = view;
    }

    // Presenter
    @Override
    public void onBack() {
        view.close();
    }
    @Override
    public void onChange(String password, String newPassword, String cnewPassword) {
        if (password.length() < 6 || newPassword.length() < 6) {
            view.showError(((Context)view).getResources().getString(R.string.password_too_short));
            return;
        }
        view.showDialog("Change..");
        SocialNetworkAPI api = Model.getSocialNetworkAPI((Context) view);
        if (api.isSignin()) {
            if (newPassword.equals(cnewPassword)) {
                api.changeMyPassword(password, newPassword, new OnChangeMyPasswordListener() {
                    @Override
                    public void onSuccess() {
                        view.clearInput();
                        view.closeDialog();
                        view.showError(((Context)view).getResources().getString(R.string.password_has_changed));
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
        } else {
            view.closeDialog();
            view.showError("No login");
        }
    }
}
