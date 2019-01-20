package com.samuigroup.originalanimewallpaper.views;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.samuigroup.originalanimewallpaper.MyFavoritesContract;
import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.models.entities.Post;
import com.samuigroup.originalanimewallpaper.presenters.MyFavoritesPresenter;
import com.samuigroup.originalanimewallpaper.views.adapters.PostsRVAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class MyFavoritesActivity extends AppCompatActivity
implements MyFavoritesContract.View {
    // Presenter
    private MyFavoritesContract.Presenter presenter;

    // Controls
    private Toolbar toolbar;
    private RecyclerView rvPosts;
    private TextView txtPageNumber;
    private Button btnNext;
    private Button btnPrevious;

    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites);

        // Presenter
        presenter = new MyFavoritesPresenter(this);

        addControls();
        addEvents();
        setToolbar();
        buildDialog();

        //
        presenter.onStart();

        //
        addBannerAd();
    }

    private void addControls() {
        rvPosts = findViewById(R.id.rv_posts);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvPosts.setLayoutManager(layoutManager);
        txtPageNumber = findViewById(R.id.txt_page_number);
        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);
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
    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            presenter.onBack();
        }
        return super.onOptionsItemSelected(item);
    }
    private void buildDialog() {
        dialog = new AlertDialog.Builder(this)
                .setCancelable(false).create();
    }

    //
    private void addBannerAd() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    // View
    @Override
    public void showPageNumber(int offset, int count) {

    }
    @Override
    public void showPosts(Post[] posts) {
        ArrayList<Post> postList = new ArrayList<>(Arrays.asList(posts));
        PostsRVAdapter adapter = new PostsRVAdapter(this, postList);
        rvPosts.setAdapter(adapter);
    }
    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void close() {
        finish();
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
