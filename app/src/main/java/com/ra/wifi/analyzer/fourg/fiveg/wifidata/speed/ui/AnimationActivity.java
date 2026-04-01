package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.BaseActivity;
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.ExtensionsKt;
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R;
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




//        AdManagerAds.CheckNative(this, getWindow().getDecorView().getRootView());
    }

    public void GetStarted(View view) {
        startActivity(new Intent(AnimationActivity.this, MainActivity.class));
        finish();
    }
}