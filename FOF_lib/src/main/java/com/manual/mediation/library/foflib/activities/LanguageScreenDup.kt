package com.manual.mediation.library.foflib.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manual.mediation.library.foflib.R
import com.manual.mediation.library.foflib.adMobAdClasses.AdmobNativeAdManager
import com.manual.mediation.library.foflib.adapters.LanguageAdapter
import com.manual.mediation.library.foflib.callingClasses.LanguageScreensConfiguration
import com.manual.mediation.library.foflib.callingClasses.SOTAdsConfigurations
import com.manual.mediation.library.foflib.callingClasses.SOTAdsManager
import com.manual.mediation.library.foflib.interfaces.CommonEventTracker
import com.manual.mediation.library.foflib.objects.StatusBarColor.STATUS_BAR_COLOR
import com.manual.mediation.library.foflib.utils.MyLocaleHelper
import com.manual.mediation.library.foflib.utils.hideSystemUIUpdated
import com.manual.mediation.library.foflib.utils.setStatusBarColor

class LanguageScreenDup: AppCompatBaseActivity() {

    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var imvDone: AppCompatImageView
    private var sotAdsConfigurations: SOTAdsConfigurations? = null
    private var tracker: CommonEventTracker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        sotAdsConfigurations = SOTAdsManager.getConfigurations()
        hideSystemUIUpdated()
        setContentView(R.layout.language_screen_dup)
        tracker = sotAdsConfigurations?.languageScreensConfiguration?.eventTracker
        tracker?.logEvent(
            this,
            "language2_scr"
        )
        Log.i("SOTStartTestActivity", "language2_scr")
        imvDone = findViewById(R.id.imvDone)
        recyclerView = findViewById(R.id.recyclerViewLanguage)
        recyclerView.layoutManager = LinearLayoutManager(this)
        onBackPressedDispatcher.addCallback(this) {
            /**Disable backPress until Home**/
        }

        LanguageScreensConfiguration.languageInstance?.let { config ->
            Log.d("fontColor", "config.tow:${config.fontColor} ")

            config.headingColor?.let {
                findViewById<TextView>(R.id.txtSelectKeyboard).setTextColor(it)
                findViewById<TextView>(R.id.txtAllLanguages).setTextColor(it)
            }
            config.tickSelector?.let {
                findViewById<AppCompatImageView>(R.id.imvDone).setImageDrawable(it)
            }
            config.theme?.let {
                val rootView = findViewById<View>(R.id.root_view)
                rootView.setBackgroundColor(it)
                setStatusBarColor(it)
            }
            config.statusBarColor?.let {
                setStatusBarColor(it)
                STATUS_BAR_COLOR = it
            }
            config.languageList?.let { languageList ->
                config.selectedDrawable?.let { selectedDrawable ->
                    config.unSelectedDrawable?.let { unSelectedDrawable ->
                        config.selectedRadio?.let { selectedRadio ->
                            config.unSelectedRadio?.let { unSelectedRadio ->
                                languageAdapter = LanguageAdapter(
                                    ctx = this,
                                    languages = languageList,
                                    selectedDrawable = selectedDrawable,
                                    unSelectedDrawable = unSelectedDrawable,
                                    selectedRadio = selectedRadio,
                                    unSelectedRadio = unSelectedRadio, onItemClickListener = {
                                            position ->
                                        val language = languageList[position]
                                        MyLocaleHelper.setLocale(this, language.code)
                                        tracker?.logEvent(
                                            this,
                                            "language2_scr_tap_language"
                                        )

                                    }
                                    , fontColor = config.fontColor!!
                                )
                                recyclerView.adapter = languageAdapter
                            }
                        }
                    }
                }
            }
        }

        imvDone.setOnClickListener {
            tracker?.logEvent(
                this,
                "language2_scr_tap_next"
            )
            Log.i("SOTStartTestActivity", "language2_scr_tap_language")
            intent?.let {
                if (it.getStringExtra("From").equals("AppSettings")) {
                    finish()
                } else {
                    SOTAdsManager.showWelcomeScreen()
                    finish()
                }
            }
        }


        if (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_SURVEY_1") as? Boolean == true) {
            loadAdmobSurveyOneNatives()
        }

        val nativeSurvey2Enabled = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_SURVEY_2") as? Boolean ?: false
        if (nativeSurvey2Enabled) {
            loadAdmobSurveyDupNatives()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                intent?.let {
//                    if (it.getStringExtra("From").equals("AppSettings")) {
                        finish()
//                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onResume() {
        super.onResume()
        if (sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_LANGUAGE_2") as? Boolean == true) {
            findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.VISIBLE
            showAdmobLanguageScreenDupNatives()
        } else {
            findViewById<CardView>(R.id.nativeAdContainerAd)?.let {
                findViewById<CardView>(R.id.nativeAdContainerAd)?.visibility = View.GONE
            }
        }
    }

    private fun showAdmobLanguageScreenDupNatives() {
        sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_LANGUAGE_2")?.let { adId ->
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "NATIVE_LANGUAGE_2",
                isMedia = true,
                isMediumAd = true,
                remoteConfig = sotAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_LANGUAGE_2").toString().toBoolean(),
                populateView = true,
                adContainer = findViewById(R.id.nativeAdContainerAd),
                onAdFailed = {
                    tracker?.logEvent(
                        this,
                        "sot_language_two_onAdFailed"
                    )
                    findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.GONE
                    Log.i("SOT_ADS_TAG", "LanguageScreenDup: Admob onAdFailed()")
                },
                onAdLoaded = {
                    tracker?.logEvent(
                        this,
                        "sot_language_two_onAdLoaded"
                    )
                    Log.i("SOT_ADS_TAG", "LanguageScreenDup: Admob onAdLoaded()")
                }
            )
        } ?: Log.w("SOT_ADS_TAG", "ADMOB_NATIVE_LANGUAGE_2 ad ID is missing.")
    }



    private fun loadAdmobSurveyOneNatives() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_SURVEY_1")
        if (adId != null) {
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "NATIVE_SURVEY_1",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } else {
            Log.w("SOT_ADS_TAG", "ADMOB_NATIVE_SURVEY_1 ad ID is missing.")
        }
    }


    private fun loadAdmobSurveyDupNatives() {
        val adId = sotAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_SURVEY_2")
        if (adId != null) {
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "NATIVE_SURVEY_2",
                isMedia = true,
                isMediumAd = true,
                populateView = false
            )
        } else {
            Log.e("SOT_ADS_TAG", "Admob ad ID not found for NATIVE_SURVEY_2")
        }
    }

}