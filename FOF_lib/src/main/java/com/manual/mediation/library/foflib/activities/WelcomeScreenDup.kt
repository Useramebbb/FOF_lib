package com.manual.mediation.library.foflib.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.cardview.widget.CardView
import com.manual.mediation.library.foflib.R
import com.manual.mediation.library.foflib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.foflib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.foflib.callingClasses.SOTAdsManager
import com.manual.mediation.library.foflib.callingClasses.WelcomeScreensConfiguration
import com.manual.mediation.library.foflib.interfaces.WelcomeDupInterface
import com.manual.mediation.library.foflib.objects.StatusBarColor.STATUS_BAR_COLOR
import com.manual.mediation.library.foflib.utils.hideSystemUIUpdated
import com.manual.mediation.library.foflib.utils.setStatusBarColor

class WelcomeScreenDup: AppCompatBaseActivity(), WelcomeDupInterface {

    private var myView: View? = null
    private var sotAdsConfigurations: SOTAdsConfigurations? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        hideSystemUIUpdated()
        onBackPressedDispatcher.addCallback(this) {
            /**Disable backPress until Home**/
        }
        setStatusBarColor(STATUS_BAR_COLOR)
        sotAdsConfigurations = SOTAdsManager.getConfigurations()

        WelcomeScreensConfiguration.welcomeInstance?.let { config ->
            config.setWelcomeDupInterface(this)
            myView = config.view
            myView?.parent?.let { parent ->
                if (parent is ViewGroup) {
                    parent.removeView(myView)
                }
            }
            setContentView(myView)
        }

        val nativeWalkThrough1Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_WALKTHROUGH_1") as? Boolean ?: false
        if (nativeWalkThrough1Enabled) {
            loadAdmobWTOneNatives()
        }
    }

    override fun onResume() {
        super.onResume()
        val nativeSurvey1Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_SURVEY_2") as? Boolean ?: false
        if (nativeSurvey1Enabled) {
            showAdmobLanguageScreenOneNatives()
        } else {
            myView?.let {
                myView?.findViewById<CardView>(R.id.nativeAdContainerAdmob)?.visibility = View.GONE
                myView?.findViewById<CardView>(R.id.nativeAdContainerMintegral)?.visibility = View.GONE
            }
        }
    }

    private fun showAdmobLanguageScreenOneNatives() {
        myView?.let {
            myView?.findViewById<CardView>(R.id.nativeAdContainerMintegral)?.visibility = View.GONE
            myView?.findViewById<CardView>(R.id.nativeAdContainerAdmob)?.visibility = View.VISIBLE
            sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_SURVEY_2")?.let { adId ->
                AdmobNativeAdManager.requestAd(
                    mContext = this,
                    adId = adId,
                    adName = "NATIVE_SURVEY_2",
                    isMedia = true,
                    isMediumAd = true,
                    remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_SURVEY_2").toString().toBoolean(),
                    populateView = true,
                    adContainer = myView?.findViewById(R.id.nativeAdContainerAdmob),
                    onAdFailed = {
                        myView?.findViewById<CardView>(R.id.nativeAdContainerAdmob)?.visibility = View.GONE
                        Log.i("SOT_ADS_TAG","WelcomeScreenDup: Admob: onAdFailed()")
                    },
                    onAdLoaded = {
                        Log.i("SOT_ADS_TAG","WelcomeScreenDup: Admob: onAdLoaded()")
                    }
                )
            } ?: Log.w("WelcomeScreenDup", "ADMOB_NATIVE_SURVEY_2 ad ID is missing.")
        }
    }
    private fun loadAdmobWTOneNatives() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_WALKTHROUGH_1")
        if (adId != null) {
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "WALKTHROUGH_1",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } else {
            Log.e("SOT_ADS_TAG","Admob ad ID not found for WALKTHROUGH_1")
        }
    }

    override fun endWelcomeTwoScreen() {
        finish()
    }
}