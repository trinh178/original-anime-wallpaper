package com.samuigroup.originalanimewallpaper.presenters;

import android.content.Context;

import com.samuigroup.originalanimewallpaper.MyFavoritesContract;
import com.samuigroup.originalanimewallpaper.models.Model;
import com.samuigroup.originalanimewallpaper.models.entities.Post;
import com.samuigroup.originalanimewallpaper.models.imagestorage.listeners.OnLoadPostsListener;

import java.util.ArrayList;

public class MyFavoritesPresenter implements MyFavoritesContract.Presenter {
    private MyFavoritesContract.View view;
    public MyFavoritesPresenter(MyFavoritesContract.View view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        view.showDialog("loading..");
        ArrayList<String> favPosts = Model.getSocialNetworkAPI((Context) view).getMyUser().getFavorites();
        Model.getImageStorageAPI((Context) view).loadPosts(favPosts.toArray(new String[favPosts.size()]), new OnLoadPostsListener() {
            @Override
            public void onSuccessful(Post[] posts) {
                view.showPosts(posts);
                view.closeDialog();
            }
            @Override
            public void onFailure(String message) {
                view.closeDialog();
                view.showError(message);
            }
        });
    }

    @Override
    public void onNextPage() {

    }

    @Override
    public void onPreviousPage() {

    }

    @Override
    public void onBack() {
        view.close();
    }
}
