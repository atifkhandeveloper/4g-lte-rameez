package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.new_ads_manager;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.R;
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.utils.Setting;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import timber.log.Timber;

public class InterstitialAdsManager {
    String mTag = "AdmobInterstitialAd";
    private static InterstitialAdsManager mInstance;
    private MyCallback myCallback;
    public InterstitialAd mInterstitialAdSplash;
    public InterstitialAd mInterstitialAdInternal;
    public InterstitialAd mInterstitialAdExit;

    public static InterstitialAdsManager getInstance() {
        if (mInstance == null) {
            mInstance = new InterstitialAdsManager();
        }
        return mInstance;
    }

    public void loadAdmobInterstitialSplash(Activity context) {
        if (!Setting.Companion.isSubActivated()){
            if (isNetworkAvailable(context)) {
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(context, context.getString(R.string.inter), adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        mInterstitialAdSplash = interstitialAd;
                        Timber.tag(mTag).d("onAdLoaded: ");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Timber.tag(mTag).d("onAdFailedToLoad: %s", loadAdError.getMessage());
                        mInterstitialAdSplash = null;
                    }
                });
            }
        }


    }

    public void showAdmobInterstitialSplash(Activity context, MyCallback myCallback1) {
        myCallback = myCallback1;

        if (mInterstitialAdSplash != null) {
            if (myCallback1 != null) {
                myCallback1.callbackCall();
            }
            mInterstitialAdSplash.show(context);

            mInterstitialAdSplash.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    Timber.tag(mTag).d("onAdFailedToShowFullScreenContent: %s", adError.getMessage());

                    mInterstitialAdSplash = null;

                    if (myCallback1 != null) {
                        myCallback1.callbackCall();
                    }
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    Timber.tag(mTag).d("onAdShowedFullScreenContent: ");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    Timber.tag(mTag).d("onAdDismissedFullScreenContent: ");
                    mInterstitialAdSplash = null;
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    Timber.tag(mTag).d("onAdImpression: ");
                }
            });
        } else {
            if (myCallback1 != null) {
                myCallback1.callbackCall();
            }
        }
    }

    public void loadAdmobInterstitialInternal(Activity context) {
        if (!Setting.Companion.isSubActivated()){
            if (isNetworkAvailable(context)) {
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(context, context.getString(R.string.inter), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    super.onAdLoaded(interstitialAd);
                    mInterstitialAdInternal = interstitialAd;
                    Timber.tag(mTag).d("onAdLoaded: ");
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Timber.tag(mTag).d("onAdFailedToLoad: %s", loadAdError.getMessage());
                    mInterstitialAdInternal = null;
                }
            });
            }
        }
    }

    public void showAdmobInterstitialInternal(boolean adAllowed, Activity context, MyCallback myCallback1) {
        myCallback = myCallback1;
        if (mInterstitialAdInternal != null) {

            mInterstitialAdInternal.show(context);

            mInterstitialAdInternal.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    Timber.tag(mTag).d("onAdFailedToShowFullScreenContent: %s", adError.getMessage());

                    mInterstitialAdInternal = null;
                    loadAdmobInterstitialInternal(context);

                    if (myCallback1 != null) {
                        myCallback1.callbackCall();
                    }
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    Timber.tag(mTag).d("onAdShowedFullScreenContent: ");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    Timber.tag(mTag).d("onAdDismissedFullScreenContent: ");
                    mInterstitialAdInternal = null;
                    loadAdmobInterstitialInternal(context);
                    if (myCallback1 != null) {
                        myCallback1.callbackCall();
                    }
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    Timber.tag(mTag).d("onAdImpression: ");
                }
            });
        } else {
            if (adAllowed)
                loadAdmobInterstitialInternal(context);
            if (myCallback1 != null) {
                myCallback1.callbackCall();
            }
        }
    }

    public void loadAdmobInterstitialExit(Activity context) {
        if (!Setting.Companion.isSubActivated()){
            if (isNetworkAvailable(context)) {
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(context, context.getString(R.string.inter), adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        mInterstitialAdExit = interstitialAd;
                        Timber.tag(mTag).d("onAdLoaded: ");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Timber.tag(mTag).d("onAdFailedToLoad: %s", loadAdError.getMessage());
                        mInterstitialAdExit = null;
                    }
                });
            }
        }
    }

    public void showAdmobInterstitialExit(Activity context, MyCallback myCallback1) {
        myCallback = myCallback1;
        if (mInterstitialAdExit != null) {
            if (myCallback1 != null) {
                myCallback1.callbackCall();
            }
            mInterstitialAdExit.show(context);

            mInterstitialAdExit.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    Timber.tag(mTag).d("onAdFailedToShowFullScreenContent: %s", adError.getMessage());
                    mInterstitialAdExit = null;
                    if (myCallback1 != null) {
                        myCallback1.callbackCall();
                    }
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    Timber.tag(mTag).d("onAdShowedFullScreenContent: ");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    Timber.tag(mTag).d("onAdDismissedFullScreenContent: ");
                    mInterstitialAdExit = null;
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    Timber.tag(mTag).d("onAdImpression: ");
                }
            });
        } else {
            if (myCallback1 != null) {
                myCallback1.callbackCall();
            }
        }
    }

    public static boolean isNetworkAvailable(Activity context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

}
