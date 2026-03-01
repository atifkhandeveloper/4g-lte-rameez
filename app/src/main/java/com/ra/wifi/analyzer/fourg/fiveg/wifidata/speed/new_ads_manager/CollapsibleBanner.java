package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;


import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R;
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.Setting;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class CollapsibleBanner {

    public static void loadBanner(Activity context, LinearLayout parentContainer, boolean adAllowed) {
        if (!Setting.Companion.isSubActivated()){
            if (isNetworkAvailable(context) && adAllowed) {
                AdView adView = new AdView(context);
                adView.setAdUnitId(context.getString(R.string.bannerId));
                AdSize adSize = getAdSize(context);
                adView.setAdSize(adSize);
                Bundle extra = new Bundle();
                extra.putString("collapsible", "bottom");
                AdRequest adRequest = new AdRequest.Builder()
                        .addNetworkExtrasBundle(AdMobAdapter.class, extra)
                        .build();
                parentContainer.addView(adView);
                adView.loadAd(adRequest);
            }
        }else{
            parentContainer.setVisibility(View.GONE);
        }

    }

    private static AdSize getAdSize(Context context) {
        float adWidthPixels;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            adWidthPixels = getDisplayWidth(context);
        } else {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            adWidthPixels = displayMetrics.widthPixels;
        }

        float density = context.getResources().getDisplayMetrics().density;
        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private static float getDisplayWidth(Context context) {
        WindowMetrics windowMetrics = context.getSystemService(WindowMetrics.class);
        if (windowMetrics != null) {
            return windowMetrics.getBounds().width();
        } else {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return displayMetrics.widthPixels;
        }
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
