package com.samuigroup.originalanimewallpaper.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.samuigroup.originalanimewallpaper.CategoryContract;
import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.models.entities.Category;
import com.samuigroup.originalanimewallpaper.presenters.CategoryPresenter;
import com.samuigroup.originalanimewallpaper.views.adapters.CategoriesRVAdapter;
import com.startapp.android.publish.adsCommon.AutoInterstitialPreferences;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.util.ArrayList;
import java.util.Arrays;

public class CategoryActivity extends AppCompatActivity
implements CategoryContract.View {
    private CategoryContract.Presenter presenter;

    private Toolbar toolbar;
    private RecyclerView rvCategory;

    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init start app ad
        StartAppSDK.init(this, getString(R.string.startapp_app_id), true);
        StartAppAd.disableSplash();
        StartAppAd.enableAutoInterstitial();
        StartAppAd.setAutoInterstitialPreferences(
                new AutoInterstitialPreferences()
                        .setSecondsBetweenAds(60)
                        .setActivitiesBetweenAds(2)
        );
        StartAppSDK.setUserConsent (this,
                "pas",
                System.currentTimeMillis(),
                true);
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        setContentView(R.layout.activity_category);

        presenter = new CategoryPresenter(this);

        setToolbar();
        addControls();
        buildDialog();

        presenter.onStart();

        //
        addBannerAd();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    private void addControls() {
        rvCategory = findViewById(R.id.rv_category);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        rvCategory.setLayoutManager(layoutManager);
    }
    private void buildDialog() {
        dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .create();
    }
    private void addBannerAd() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void showError(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showCategories(Category[] categories) {
        ArrayList<Category> cat = new ArrayList<>(Arrays.asList(categories));
        CategoriesRVAdapter adapter = new CategoriesRVAdapter(this, cat);
        rvCategory.setAdapter(adapter);
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
    @Override
    public void close() {
        finish();
    }
}
