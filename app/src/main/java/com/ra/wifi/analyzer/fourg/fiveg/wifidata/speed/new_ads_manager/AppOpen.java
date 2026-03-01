//package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager;
//
//import android.app.Activity;
//import android.app.Application;
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import androidx.annotation.NonNull;
//import androidx.lifecycle.DefaultLifecycleObserver;
//import androidx.lifecycle.LifecycleOwner;
//import androidx.lifecycle.ProcessLifecycleOwner;
//
//import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R;
//import com.google.android.gms.ads.AdError;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.FullScreenContentCallback;
//import com.google.android.gms.ads.LoadAdError;
//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.appopen.AppOpenAd;
//import com.google.android.gms.ads.initialization.InitializationStatus;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
//
//import java.util.Date;
//
//public class AppOpen extends Application implements Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {
//    private AppOpenAdManager appOpenAdManager;
//    private Activity currentActivity;
//    private static final String TAG = "MyApplication";
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        this.registerActivityLifecycleCallbacks(this);
//
//        Log.d(TAG, "Google Mobile Ads SDK Version: " + MobileAds.getVersion());
//
//        MobileAds.initialize(
//                this,
//                new OnInitializationCompleteListener() {
//                    @Override
//                    public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
//                        // Initialization completed
//                    }
//                });
//
//        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
//        appOpenAdManager = new AppOpenAdManager();
//    }
//
//    @Override
//    public void onStart(@NonNull LifecycleOwner owner) {
//        DefaultLifecycleObserver.super.onStart(owner);
//        appOpenAdManager.showAdIfAvailable(currentActivity);
//    }
//
//    @Override
//    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {}
//
//    @Override
//    public void onActivityStarted(@NonNull Activity activity) {
//        if (!appOpenAdManager.isShowingAd) {
//            currentActivity = activity;
//        }
//    }
//
//    @Override
//    public void onActivityResumed(@NonNull Activity activity) {
//
//    }
//
//    @Override
//    public void onActivityPaused(@NonNull Activity activity) {}
//
//    @Override
//    public void onActivityStopped(@NonNull Activity activity) {}
//
//    @Override
//    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}
//
//    @Override
//    public void onActivityDestroyed(@NonNull Activity activity) {}
//
//    public void showAdIfAvailable(@NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
//        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
//    }
//
//    public interface OnShowAdCompleteListener {
//        void onShowAdComplete();
//    }
//
//    private class AppOpenAdManager {
//        private static final String LOG_TAG = "AppOpenAdManager";
////        private static final String AD_UNIT_ID = SharedPref.getAdmobOpenad(this);
//        private AppOpenAd appOpenAd = null;
//        private boolean isLoadingAd = false;
//        public boolean isShowingAd = false;
//        private long loadTime = 0;
//
//        private void loadAd(Context context) {
//            if (isLoadingAd || isAdAvailable()) {
//                return;
//            }
//
//            isLoadingAd = true;
//            AdRequest request = new AdRequest.Builder().build();
//            AppOpenAd.load(
//                    context, getString(R.string.appOpenAds),
//                    request,
//                    new AppOpenAd.AppOpenAdLoadCallback() {
//                        @Override
//                        public void onAdLoaded(AppOpenAd ad) {
//                            appOpenAd = ad;
//                            isLoadingAd = false;
//                            loadTime = (new Date()).getTime();
//                            Log.d(LOG_TAG, "onAdLoaded.");
//                        }
//
//                        @Override
//                        public void onAdFailedToLoad(LoadAdError loadAdError) {
//                            isLoadingAd = false;
//                            Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
//                        }
//                    });
//        }
//
//        private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
//            long dateDifference = (new Date()).getTime() - loadTime;
//            long numMilliSecondsPerHour = 3600000;
//            return (dateDifference < (numMilliSecondsPerHour * numHours));
//        }
//
//        private boolean isAdAvailable() {
//            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
//        }
//
//        private void showAdIfAvailable(@NonNull final Activity activity) {
//            showAdIfAvailable(
//                    activity,
//                    new OnShowAdCompleteListener() {
//                        @Override
//                        public void onShowAdComplete() {
//                            // Empty because the user will go back to the activity that shows the ad.
//                        }
//                    });
//        }
//
//
//        private void showAdIfAvailable(
//                @NonNull final Activity activity,
//                @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
//            if (isShowingAd) {
//                Log.d(LOG_TAG, "The app open ad is already showing.");
//                return;
//            }
//
//            if (!isAdAvailable()) {
//                Log.d(LOG_TAG, "The app open ad is not ready yet.");
//                onShowAdCompleteListener.onShowAdComplete();
//                loadAd(activity);
//                return;
//            }
//
//            Log.d(LOG_TAG, "Will show ad.");
//
//            appOpenAd.setFullScreenContentCallback(
//                    new FullScreenContentCallback() {
//                        @Override
//                        public void onAdDismissedFullScreenContent() {
//                            appOpenAd = null;
//                            isShowingAd = false;
//                            onShowAdCompleteListener.onShowAdComplete();
//                            loadAd(activity);
//                        }
//
//                        @Override
//                        public void onAdFailedToShowFullScreenContent(AdError adError) {
//                            appOpenAd = null;
//                            isShowingAd = false;
//                            onShowAdCompleteListener.onShowAdComplete();
//                            loadAd(activity);
//                        }
//
//                        @Override
//                        public void onAdShowedFullScreenContent() {}
//                    });
//
//            isShowingAd = true;
//            appOpenAd.show(activity);
//        }
//    }
//}
