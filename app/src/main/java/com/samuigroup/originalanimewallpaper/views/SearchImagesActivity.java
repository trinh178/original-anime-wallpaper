package com.samuigroup.originalanimewallpaper.views;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.samuigroup.originalanimewallpaper.BaseContract;
import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.SearchImagesContract;
import com.samuigroup.originalanimewallpaper.models.entities.Post;
import com.samuigroup.originalanimewallpaper.presenters.SearchImagesPresenter;
import com.samuigroup.originalanimewallpaper.views.adapters.PostsRVAdapter;
import com.startapp.android.publish.ads.banner.Banner;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchImagesActivity extends BaseActivity
implements SearchImagesContract.View {

    private RecyclerView rvPosts;
    private SearchImagesContract.Presenter presenter;
    private TextView txtPageNumber;
    private Button btnNext;
    private Button btnPrevious;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Presenter
        presenter = new SearchImagesPresenter(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Views init
        rvPosts = findViewById(R.id.rv_posts);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvPosts.setLayoutManager(layoutManager);
        txtPageNumber = findViewById(R.id.txt_page_number);
        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);

        addEvents();
        buildDialog();
        addBannerAd();

        //
        String[] tags = new String[] {};
        if (getIntent().hasExtra("tags"))
            tags = (String[]) getIntent().getSerializableExtra("tags");
        presenter.onStart(tags);
    }

    // Presenter
    @Override
    protected BaseContract.Presenter getBasePresenter() {
        return presenter;
    }

    private void addEvents() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onNextPage();
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPreviousPage();
            }
        });
    }
    private void buildDialog() {
        dialog = new AlertDialog.Builder(this)
                .setCancelable(false).create();
    }
    private void addBannerAd() {
        Banner adBanner = findViewById(R.id.banner);
        //adBanner.loadAd();
    }

    // Base API
    @Override
    protected void onSearchClickListener(String[] tags) {
        presenter.onSearch(tags, 10, 1);
    }

    // View
    @Override
    public void showPosts(Post[] posts) {
        ArrayList<Post> postList = new ArrayList<>(Arrays.asList(posts));
        PostsRVAdapter adapter = new PostsRVAdapter(this, postList);
        rvPosts.setAdapter(adapter);
    }
    @Override
    public void showPageNumber(int offset, int count) {
        txtPageNumber.setText(offset + " / " + count + " " + getResources().getString(R.string.pages));
    }
    @Override
    public void openPostView(Post post) {

    }
    @Override
    public void showDialog(String text) {
        dialog.setMessage(text);
        dialog.show();
    }
    @Override
    public void closeDialog() {
        dialog.dismiss();
    }
}
