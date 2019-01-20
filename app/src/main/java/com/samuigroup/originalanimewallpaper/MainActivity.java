package com.samuigroup.originalanimewallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.samuigroup.originalanimewallpaper.views.CategoryActivity;
import com.startapp.android.publish.adsCommon.AutoInterstitialPreferences;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

public class MainActivity extends AppCompatActivity {
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

        setContentView(R.layout.activity_main);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable, 2000);
    }
}
