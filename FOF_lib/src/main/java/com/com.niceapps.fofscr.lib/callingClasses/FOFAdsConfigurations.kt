package com.niceapps.fofscr.lib.callingClasses

import android.app.Activity
import android.util.Log
import com.niceapps.fofscr.lib.adMobAdClasses.AdmobInterstitialAdSplash
import com.niceapps.fofscr.lib.adMobAdClasses.AdmobResumeAdSplash
import com.niceapps.fofscr.lib.adMobAdClasses.AdmobNativeAdManager
import com.niceapps.fofscr.lib.utils.NetworkCheck
import com.niceapps.fofscr.lib.utils.PrefHelper
import com.niceapps.fofscr.lib.utilsGoogleAdsConsent.ConsentConfigurations

class FOFAdsConfigurations private constructor() {

    var languageScreensConfiguration: LanguageScreensConfiguration? = null
    var welcomeScreensConfiguration: WelcomeScreensConfiguration? = null
    var walkThroughScreensConfiguration: WalkThroughScreensConfiguration? = null
    private var remoteConfigData: HashMap<String, Any>? = null
    var firstOpenFlowAdIds: HashMap<String, String> = HashMap()
    private lateinit var admobResumeAdSplash: AdmobResumeAdSplash
    private lateinit var admobInterstitialAdSplash: AdmobInterstitialAdSplash

    fun setRemoteConfigData(activityContext: Activity, myRemoteConfigData: HashMap<String, Any>) {
        this.remoteConfigData = myRemoteConfigData
        this.remoteConfigData?.forEach { value ->
            Log.i("RemoteConfigFetches","AdsConfigurations : setRemoteConfigData() "+value.key + " : " + value.value)
        }

        if (NetworkCheck.isNetworkAvailable(activityContext)) {
            when {
                myRemoteConfigData.getValue("RESUME_INTER_SPLASH") == "RESUME" -> {
                    showAdMobResumeAdSplash(activityContext)
                }
                myRemoteConfigData.getValue("RESUME_INTER_SPLASH") == "INTERSTITIAL" -> {
                    showAdMobInterstitialAdSplash(activityContext)
                }
                myRemoteConfigData.getValue("RESUME_INTER_SPLASH") == "OFF" -> {
                    proceedNext(activityContext)
                }
            }

            if (!PrefHelper(activityContext).getBooleanDefault("StartScreens", default = false)) {
                if (myRemoteConfigData.getValue("NATIVE_LANGUAGE_1") == true) {
                    loadAdmobLanguageScreenOneNatives(activityContext)
                }
                if (myRemoteConfigData.getValue("NATIVE_LANGUAGE_2") == true){
                    loadAdmobLanguageScreenDupNatives(activityContext)
                }

            }
        } else {
            proceedNext(activityContext)
        }
    }

    private fun loadAdmobLanguageScreenOneNatives(mContext: Activity) {
         AdmobNativeAdManager.requestAd(
             mContext = mContext,
             adId = firstOpenFlowAdIds.getValue("ADMOB_NATIVE_LANGUAGE_1"),
             adName = "NATIVE_LANGUAGE_1",
             isMedia = true,
             isMediumAd = true,
             populateView = false
         )
    }

    private fun loadAdmobLanguageScreenDupNatives(mContext: Activity) {
        AdmobNativeAdManager.requestAd(
            mContext = mContext,
            adId = firstOpenFlowAdIds.getValue("ADMOB_NATIVE_LANGUAGE_2"),
            adName = "NATIVE_LANGUAGE_2",
            isMedia = true,
            isMediumAd = true,
            populateView = false
        )
    }


    private fun showAdMobResumeAdSplash(activityContext: Activity) {
        activityContext.let {
            admobResumeAdSplash = AdmobResumeAdSplash(activityContext, firstOpenFlowAdIds.getValue("ADMOB_SPLASH_RESUME"),
                onAdDismissed = {
                    proceedNext(activityContext)
                },
                onAdFailed = {
                    proceedNext(activityContext)
                },
                onAdTimeout = {
                    proceedNext(activityContext)
                },
                onAdShowed = {}
            )
        }
    }


    private fun showAdMobInterstitialAdSplash(activityContext: Activity) {
        activityContext.let {
            admobInterstitialAdSplash = AdmobInterstitialAdSplash(activityContext, firstOpenFlowAdIds.getValue("ADMOB_SPLASH_INTERSTITIAL"),
                onAdDismissed = {
                    proceedNext(activityContext)
                },
                onAdFailed = {
                    proceedNext(activityContext)
                },
                onAdTimeout = {
                    proceedNext(activityContext)
                },
                onAdShowed = {}
            )
        }
    }


    private fun proceedNext(activityContext: Activity) {
        if (PrefHelper(activityContext).getBooleanDefault("StartScreens", default = false)) {
            FOFAdsManager.notifyFlowFinished()
        } else {
            startLanguageScreenConfiguration()
        }
    }

    fun getRemoteConfigData() : HashMap<String, Any>? {
        return this.remoteConfigData
    }

    private fun startLanguageScreenConfiguration() {
        Log.i("LanguageScreens","fofAdsConfigurations : startLanguageScreenConfiguration() ")
        this.languageScreensConfiguration?.languageInitializationSetup() ?: run { }
    }

    fun startWelcomeScreenConfiguration() {
        Log.i("WelcomeScreens","fofAdsConfigurations : startWelcomeScreenConfiguration() ")
        FOFAdsManager.notifyReConfigureBuilders()
        this.welcomeScreensConfiguration?.welcomeInitializationSetup() ?: run { }
    }

    fun startWalkThroughConfiguration() {
        Log.i("WalkThroughScreens","fofAdsConfigurations : startWalkThroughConfiguration() ")
        this.walkThroughScreensConfiguration?.walkThroughInitializationSetup() ?: run { }
    }

    class Builder {
        private lateinit var consentConfigurations: ConsentConfigurations
        private var languageScreensConfiguration: LanguageScreensConfiguration? = null
        private var welcomeScreensConfiguration: WelcomeScreensConfiguration? = null
        private var walkThroughScreensConfiguration: WalkThroughScreensConfiguration? = null
        private var firstOpenFlowAdIds: HashMap<String, String> = HashMap()

        fun setFirstOpenFlowAdIds(firstOpenFlowAdIds: HashMap<String, String>) = apply {
            this.firstOpenFlowAdIds = firstOpenFlowAdIds
            this.firstOpenFlowAdIds.forEach { value ->
                Log.i("NICE_APPS_ADS_TAG","setFirstOpenFlowAdIds : "+value.key + " : " + value.value)
            }
        }

        fun setConsentConfig(consentConfig: ConsentConfigurations) = apply {
            this.consentConfigurations = consentConfig
        }

        fun setLanguageScreenConfiguration(languageScreensConfiguration: LanguageScreensConfiguration) = apply {
            this.languageScreensConfiguration = languageScreensConfiguration
        }

        fun setWelcomeScreenConfiguration(welcomeScreensConfiguration: WelcomeScreensConfiguration) = apply {
            this.welcomeScreensConfiguration = welcomeScreensConfiguration
        }

        fun setWalkThroughScreenConfiguration(walkThroughScreensConfiguration: WalkThroughScreensConfiguration) = apply {
            this.walkThroughScreensConfiguration = walkThroughScreensConfiguration
        }

        fun build(): FOFAdsConfigurations {
            if (!::consentConfigurations.isInitialized) {
                throw IllegalStateException("ConsentConfigurations must be provided")
            }
            val fofAdsConfigurations = FOFAdsConfigurations()
            fofAdsConfigurations.firstOpenFlowAdIds = firstOpenFlowAdIds
            fofAdsConfigurations.welcomeScreensConfiguration = welcomeScreensConfiguration
            fofAdsConfigurations.languageScreensConfiguration = languageScreensConfiguration
            fofAdsConfigurations.walkThroughScreensConfiguration = walkThroughScreensConfiguration
            return fofAdsConfigurations
        }
    }
}
