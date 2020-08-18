package com.awkwardlydevelopedapps.unicharsheet;

import android.content.Context;
import android.os.Bundle;

import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class AdSingleton {
    private static AdSingleton instance = null;

    private ConsentForm consentForm;

    private AdSingleton() {

    }

    public static AdSingleton Instance() {
        if (instance == null) {
            synchronized (AdSingleton.class) {
                instance = new AdSingleton();
            }
        }

        return instance;
    }

    public void adInit(Context context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
    }

    public void enableTestDevice() {
        List<String> testDeviceIds = Arrays.asList("0D9DC6373F50EC8ACEDB84403476DA34");
        RequestConfiguration configuration = new RequestConfiguration.Builder()
                .setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
    }

    public void adLoad(Context context, AdView adView) {

        AdRequest adRequest;

        Bundle extras = new Bundle();
        ConsentInformation consentInformation = ConsentInformation.getInstance(context);
        if (consentInformation.getConsentStatus().equals(ConsentStatus.NON_PERSONALIZED)) {
            extras.putString("npa", "1");

            adRequest = new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .build();
        } else {
            adRequest = new AdRequest.Builder()
                    .build();
        }

        adView.loadAd(adRequest);
    }

    public void consentInfoUpdate(Context context) {
        ConsentInformation.getInstance(context).addTestDevice("AE5B3676F36A10E4DE9250343E2B2B70");
        //ConsentInformation.getInstance(context).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_NOT_EEA);
        //ConsentInformation.getInstance(this).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);

        final ConsentInformation consentInformation = ConsentInformation.getInstance(context);
        String[] publisherIds = {BuildConfig.PUBLISHER_ID};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                if (consentStatus == ConsentStatus.UNKNOWN) {
                    //Build and display form.
                    consentFormBuild(context);
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String reason) {

            }
        });
    }

    private void consentFormBuild(Context context) {
        URL privacyUrl = null;
        try {
            privacyUrl = new URL("https://awkwardlydevelopedapps.000webhostapp.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }
        consentForm = new ConsentForm.Builder(context, privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.
                        consentForm.show();
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                    }

                    @Override
                    public void onConsentFormClosed(ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .build();

        consentForm.load();
    }

    public void resetConsentOption(Context context) {
        ConsentInformation.getInstance(context).reset();
        consentInfoUpdate(context);
    }
}
