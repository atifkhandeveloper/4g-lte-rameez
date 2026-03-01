package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity;
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ExtensionsKt;
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R;
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager.NativeAdsManager;
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.ConfigParam;

public class AnimationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(AnimationActivity.this, MainActivity.class));
//                finish();
//            }
//        },5000);

        String nativeAdId = getString(R.string.nativeId);
        NativeAdsManager.ReqLoadNativeAd(ExtensionsKt.isAdEnable(getConfig(), ConfigParam.NATIVE_ANIMATION),
                this, getWindow().getDecorView().getRootView(), nativeAdId);


//        AdManagerAds.CheckNative(this, getWindow().getDecorView().getRootView());
    }

    public void GetStarted(View view) {
        startActivity(new Intent(AnimationActivity.this, MainActivity.class));
        finish();
    }
}