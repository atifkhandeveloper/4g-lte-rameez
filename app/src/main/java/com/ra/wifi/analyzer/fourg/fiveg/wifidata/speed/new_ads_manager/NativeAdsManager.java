package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;


import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R;
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.Setting;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.Objects;

public class NativeAdsManager {

    public static void ReqLoadNativeAd(boolean adAllowed, Activity activity, View view, String nativeAdId) {
        CardView nativeContainer = view.findViewById(R.id.native_ad);
        if (!Setting.Companion.isSubActivated()){
           if (isNetworkAvailable(activity) && adAllowed) {
                nativeContainer.setVisibility(View.VISIBLE);
                LoadNativeAd(activity, nativeContainer, nativeAdId);
            } else {
                nativeContainer.setVisibility(View.GONE);
            }
        }else{
            nativeContainer.setVisibility(View.GONE);
        }

    }

    public static void LoadNativeAd(Activity mActivity, FrameLayout frameLayout, String nativeAdId) {
        AdLoader adLoader = new AdLoader.Builder(mActivity, nativeAdId).forNativeAd(nativeAd -> {
            @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) mActivity.getLayoutInflater().inflate(R.layout.item_small_native, null);

            showNativeAd(nativeAd, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        }).withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                frameLayout.setVisibility(View.VISIBLE);
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private static void showNativeAd(NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView(adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
        ((TextView) Objects.requireNonNull(adView.getBodyView())).setText(nativeAd.getBody());
        ((TextView) Objects.requireNonNull(adView.getCallToActionView())).setText(nativeAd.getCallToAction());

        if (nativeAd.getIcon() != null) {
            ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(nativeAd.getIcon().getDrawable());
        }

        ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());

        adView.setClickable(true);
        adView.setOnClickListener(view -> {
            // Handle ad click
        });

        adView.setNativeAd(nativeAd);
    }

    public static boolean isNetworkAvailable(Activity context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            //noinspection deprecation
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

}