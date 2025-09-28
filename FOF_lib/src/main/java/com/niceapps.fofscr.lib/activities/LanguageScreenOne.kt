package com.niceapps.fofscr.lib.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.niceapps.fofscr.lib.R

import com.niceapps.fofscr.lib.adMobAdClasses.AdmobNativeAdManager
import com.niceapps.fofscr.lib.adapters.LanguageAdapter
import com.niceapps.fofscr.lib.callingClasses.LanguageScreensConfiguration
import com.niceapps.fofscr.lib.callingClasses.FOFAdsConfigurations
import com.niceapps.fofscr.lib.callingClasses.FOFAdsManager
import com.niceapps.fofscr.lib.interfaces.CommonEventTracker
import com.niceapps.fofscr.lib.interfaces.LanguageInterface
import com.niceapps.fofscr.lib.objects.StatusBarColor.STATUS_BAR_COLOR
import com.niceapps.fofscr.lib.utils.hideSystemUIUpdated
import com.niceapps.fofscr.lib.utils.setStatusBarColor

class LanguageScreenOne : AppCompatBaseActivity(), LanguageInterface {

    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var recyclerView: RecyclerView
    private var fofAdsConfigurations: FOFAdsConfigurations? = null
    private var tracker:CommonEventTracker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fofAdsConfigurations = FOFAdsManager.getConfigurations()
        tracker = fofAdsConfigurations?.languageScreensConfiguration?.eventTracker
        supportActionBar?.hide()
        hideSystemUIUpdated()
        setContentView(R.layout.language_screen_one)
        tracker?.logEvent(
            this,
            "language1_scr"
        )
        Log.i("StartTestActivity", "language1_scr")
        onBackPressedDispatcher.addCallback(this) {
            /**Disable backPress until Home**/
        }
        recyclerView = findViewById(R.id.recyclerViewLanguage)
        recyclerView.layoutManager = LinearLayoutManager(this)

        Log.i("LanguageScreenOne", "Language: onCreate")

        LanguageScreensConfiguration.languageInstance?.let { config ->
            config.setLanguageInterface(this)
            Log.d("fontColor", "config.fontColor:${config.headingColor} ")
            config.headingColor?.let {
                findViewById<TextView>(R.id.txtSelectKeyboard).setTextColor(it)
                findViewById<TextView>(R.id.txtAllLanguages).setTextColor(it)
            }

            config.theme?.let {
                val rootView = findViewById<View>(R.id.root_view)
                rootView.setBackgroundColor(it)
                STATUS_BAR_COLOR = it
            }
            config.statusBarColor?.let {
                setStatusBarColor(it)
                STATUS_BAR_COLOR = it
            }
            config.languageList?.let {
                if (config.selectedDrawable != null && config.unSelectedDrawable != null) {
                    if (config.selectedRadio != null && config.unSelectedRadio != null) {
                        languageAdapter = LanguageAdapter(
                            ctx = this,
                            languages = config.languageList!!,
                            selectedDrawable = config.selectedDrawable!!,
                            unSelectedDrawable = config.unSelectedDrawable!!,
                            selectedRadio = config.selectedRadio!!,
                            unSelectedRadio = config.unSelectedRadio!!, onItemClickListener = {
                                tracker?.logEvent(
                                    this,
                                    "language1_scr_tap_language"
                                )
                                config.showLanguageTwoScreen()
                            }, fontColor = config.fontColor!!)


                        recyclerView.adapter = languageAdapter
                    }
                }
            }
        }


    }




    override fun showLanguageTwoScreen() {
        if (!isFinishing && !isDestroyed) {
            try {
                val intent = Intent(this, LanguageScreenDup::class.java)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent, ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                    finish()
                } else {
                    Log.e("LanguageScreenOne", "No activity found to handle intent")
                }
            } catch (e: Exception) {
                Log.e("LanguageScreenOne", "Error starting activity", e)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val nativeLanguage1Enabled = fofAdsConfigurations?.getRemoteConfigData()?.get("NATIVE_LANGUAGE_1") as? Boolean ?: false
        if (nativeLanguage1Enabled) {
            showAdmobLanguageScreenOneNatives()
        } else {
            findViewById<CardView>(R.id.nativeAdContainerAd)?.let {
                findViewById<CardView>(R.id.nativeAdContainerAd)?.visibility = View.GONE
            }
        }
    }

    private fun showAdmobLanguageScreenOneNatives() {
        fofAdsConfigurations?.firstOpenFlowAdIds?.getValue("ADMOB_NATIVE_LANGUAGE_1")?.let { adId ->
            AdmobNativeAdManager.requestAd(
                mContext = this,
                adId = adId,
                adName = "NATIVE_LANGUAGE_1",
                isMedia = true,
                isMediumAd = true,
                remoteConfig = fofAdsConfigurations?.getRemoteConfigData()?.getValue("NATIVE_LANGUAGE_1").toString().toBoolean(),
                populateView = true,
                adContainer = findViewById(R.id.nativeAdContainerAd),
                onAdFailed = {
                    findViewById<CardView>(R.id.nativeAdContainerAd).visibility = View.GONE
                    Log.i("LanguageScreenOne", "Language: onAdFailed()")
                },
                onAdLoaded = {
                    tracker?.logEvent(
                        this,
                        "fof_language_0ne_adShown"
                    )
                    Log.i("LanguageScreenOne", "Language: onAdLoaded()")
                }
            )
        } ?: Log.w("LanguageScreenOne", "ADMOB_NATIVE_LANGUAGE_1 ad ID is missing.")
    }


}