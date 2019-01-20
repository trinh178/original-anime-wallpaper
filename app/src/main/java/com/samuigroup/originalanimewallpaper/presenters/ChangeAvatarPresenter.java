package com.samuigroup.originalanimewallpaper.presenters;

import android.content.Context;
import android.graphics.Bitmap;

import com.samuigroup.originalanimewallpaper.ChangeAvatarContract;
import com.samuigroup.originalanimewallpaper.models.Model;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnChangeMyAvatarListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadAvatarListener;

public class ChangeAvatarPresenter implements ChangeAvatarContract.Presenter {
    private ChangeAvatarContract.View view;

    public ChangeAvatarPresenter(ChangeAvatarContract.View view) {
        this.view = view;
    }

    // Presenter
    @Override
    public void onStart() {
        Model.getSocialNetworkAPI((Context) view).loadAvatar(Model.getSocialNetworkAPI((Context) view).getMyUser().getUsername(),
                new OnLoadAvatarListener() {
                    @Override
                    public void onSuccess(Bitmap bmp) {
                        view.showAvatar(bmp);
                    }
                    @Override
                    public void onFailure(String msg) {
                        view.showError(msg);
                    }
                });
    }
    @Override
    public void onBack() {
        view.close();
    }
    @Override
    public void onChange(final Bitmap bmp) {
        view.showDialog("Change..");
        Model.getSocialNetworkAPI((Context) view).changeMyAvatar(bmp, new OnChangeMyAvatarListener() {
            @Override
            public void onSuccess() {
                view.closeDialog();
                view.showAvatar(bmp);
            }
            @Override
            public void onFailure(String msg) {
                view.closeDialog();
                view.showError(msg);
            }
        });
    }
}
