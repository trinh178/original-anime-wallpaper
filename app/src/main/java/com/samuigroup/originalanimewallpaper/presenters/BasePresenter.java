package com.samuigroup.originalanimewallpaper.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.samuigroup.originalanimewallpaper.BaseContract;
import com.samuigroup.originalanimewallpaper.models.Model;
import com.samuigroup.originalanimewallpaper.models.imagestorage.ImageStorageAPI;
import com.samuigroup.originalanimewallpaper.models.imagestorage.KonachanWebservice;
import com.samuigroup.originalanimewallpaper.models.imagestorage.listeners.OnSearchTagsListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadAvatarListener;
import com.samuigroup.originalanimewallpaper.utils.Utils;

public abstract class BasePresenter implements BaseContract.Presenter {
    private BaseContract.View view;

    // Constructors
    public BasePresenter(BaseContract.View view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        if (Model.getSocialNetworkAPI((Context) view).isSignin()) {
            Model.getSocialNetworkAPI((Context) view).loadAvatar(Model.getSocialNetworkAPI((Context) view).getMyUser().getUsername(),
                    new OnLoadAvatarListener() {
                        @Override
                        public void onSuccess(Bitmap bmp) {
                            view.showAvatar(bmp);
                        }

                        @Override
                        public void onFailure(String msg) {
                            Utils.Log(msg);
                        }
                    });
            view.showUsername(Model.getSocialNetworkAPI((Context) view).getMyUser().getUsername());
            view.setNavigationMenu(true);
        } else {
            view.setNavigationMenu(false);
        }
    }

    // Presenter
    @Override
    public void onTypeSearch(String name) {
        ImageStorageAPI loader = KonachanWebservice.getInstance((Context) view);
        loader.searchTags(name, 10, new OnSearchTagsListener() {
            @Override
            public void onSuccessful(String[] tags) {
                view.showTagsSuggestion(tags);
            }

            @Override
            public void onFailure(String message) {
                Log.d("TEST", message);
            }
        });
    }

    @Override
    public void onNavClickChangeAvatar() {
        view.openChangeAvatarView();
    }
    @Override
    public void onNavClickChangePassword() {
        view.openChangePasswordView();
    }
    @Override
    public void onNavClickImagesFavorite() {
        view.openMyFavoritesView();
    }
    @Override
    public void onNavClickSignout() {
        Model.getSocialNetworkAPI((Context) view).signout();
        view.setNavigationMenu(false);
    }
    @Override
    public void onNavClickExit() {
        view.close();
    }
    @Override
    public void onNavClickSignin() {
        view.openSigninView();
    }
    @Override
    public void onNavClickSignup() {
        view.openSignupView();
    }
    @Override
    public void onNavClickCategories() {
        view.openCategories();
    }
}
