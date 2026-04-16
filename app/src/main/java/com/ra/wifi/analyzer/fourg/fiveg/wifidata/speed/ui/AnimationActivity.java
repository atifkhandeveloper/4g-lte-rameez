package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity;
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ExtensionsKt;
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R;
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam;

public class AnimationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);



        loadnative();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(AnimationActivity.this, MainActivity.class));
//                finish();
//            }
//        },5000);




//        AdManagerAds.CheckNative(this, getWindow().getDecorView().getRootView());
    }

    public void GetStarted(View view) {
        startActivity(new Intent(AnimationActivity.this, PremiumActivity.class));
        finish();
    }

    private void loadnative() {

        MobileAds.initialize(this);

        // Optional: set background color
        ColorDrawable background = new ColorDrawable(Color.WHITE);

        // Create AdLoader
        AdLoader adLoader = new AdLoader.Builder(this, getResources().getString(R.string.nativeId))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {

                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder()
                                .withMainBackgroundColor(background)
                                .build();

                        TemplateView template = findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                    }
                })
                .build();

        // Load Ad
        adLoader.loadAd(new AdRequest.Builder().build());
    }
}