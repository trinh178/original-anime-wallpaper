package com.samuigroup.originalanimewallpaper.presenters;

import android.content.Context;

import com.samuigroup.originalanimewallpaper.SearchImagesContract;
import com.samuigroup.originalanimewallpaper.models.entities.Post;
import com.samuigroup.originalanimewallpaper.models.imagestorage.ImageStorageAPI;
import com.samuigroup.originalanimewallpaper.models.imagestorage.KonachanWebservice;
import com.samuigroup.originalanimewallpaper.models.imagestorage.listeners.OnSearchPostsListener;
import com.samuigroup.originalanimewallpaper.utils.Utils;

public class SearchImagesPresenter extends BasePresenter implements SearchImagesContract.Presenter {
    private SearchImagesContract.View view;
    // Params
    private String[] tags;
    private int limit;
    private int page;
    private int count;

    // Constructors
    public SearchImagesPresenter(SearchImagesContract.View view) {
        super(view);
        this.view = view;
    }

    // Presenter
    @Override
    public void onStart(String[] tags) {
        onSearch(tags, 10, 1);
    }
    @Override
    public void onNextPage() {
        if (page < count/limit)
            onSearch(tags, limit, page + 1);
    }
    @Override
    public void onPreviousPage() {
        if (page > 1)
            onSearch(tags, limit, page - 1);
    }
    @Override
    public void onSearch(String[] tags, int limit, int page) {
        view.showDialog("loading..");

        // Saved search tags
        view.setTagsText(Utils.joinTags(tags, ", "));

        //
        ImageStorageAPI loader = KonachanWebservice.getInstance((Context) view);
        loader.searchPosts(tags, limit, page, new OnSearchPostsListener() {
            @Override
            public void onSuccessful(Post[] posts, int limit, int page, int count) {
                view.showPosts(posts);
                view.showPageNumber(page, count/limit);
                view.closeDialog();
                // Params
                SearchImagesPresenter.this.count = count;
            }

            @Override
            public void onFailure(String message) {
                view.closeDialog();
                Utils.Log(message);
            }
        });
        // Params
        this.tags = tags;
        this.limit = limit;
        this.page = page;
    }
}
