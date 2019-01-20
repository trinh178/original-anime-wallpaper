package com.samuigroup.originalanimewallpaper.presenters;

import android.content.Context;

import com.samuigroup.originalanimewallpaper.CategoryContract;
import com.samuigroup.originalanimewallpaper.models.Model;
import com.samuigroup.originalanimewallpaper.models.entities.Category;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadCategoriesListener;

public class CategoryPresenter implements CategoryContract.Presenter {
    private CategoryContract.View view;

    public CategoryPresenter(CategoryContract.View view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        view.showDialog("loading..");
        Model.getSocialNetworkAPI((Context) view).loadCategories(new OnLoadCategoriesListener() {
            @Override
            public void onSuccess(Category[] categories) {
                view.showCategories(categories);
                view.closeDialog();
            }
            @Override
            public void onFailure(String msg) {
                view.closeDialog();
                view.showError(msg);
            }
        });
    }
}
