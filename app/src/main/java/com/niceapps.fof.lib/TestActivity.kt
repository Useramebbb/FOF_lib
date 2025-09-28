package com.niceapps.fof.lib

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.niceapps.fofscr.lib.activities.AppCompatBaseActivity
import com.niceapp.fof.lib.R
import com.niceapps.fofscr.lib.adMobAdClasses.AdMobBannerAdSplash
import com.niceapps.fofscr.lib.callingClasses.LanguageScreensConfiguration
import com.niceapps.fofscr.lib.callingClasses.FOFAdsConfigurations
import com.niceapps.fofscr.lib.callingClasses.FOFAdsManager
import com.niceapps.fofscr.lib.callingClasses.WalkThroughScreensConfiguration
import com.niceapps.fofscr.lib.callingClasses.WelcomeScreensConfiguration
import com.niceapps.fofscr.lib.data.Language
import com.niceapps.fofscr.lib.data.WalkThroughItem
import com.niceapps.fofscr.lib.utils.FirebaseCommonTracker
import com.niceapps.fofscr.lib.utils.MyLocaleHelper
import com.niceapps.fofscr.lib.utils.NetworkCheck
import com.niceapps.fofscr.lib.utils.PrefHelper
import com.niceapps.fofscr.lib.utils.hideSystemUIUpdated
import com.niceapps.fofscr.lib.utilsGoogleAdsConsent.ConsentConfigurations
import com.niceapp.fof.lib.databinding.ActivityStartTestBinding
import com.urdu_keyboard.utilityClasses.RemoteConfigConstTest

class TestActivity : AppCompatBaseActivity() {

    private lateinit var binding: ActivityStartTestBinding
    private lateinit var fofAdsConfigurations: FOFAdsConfigurations
    private var firstOpenFlowAdIds: HashMap<String, String> = HashMap()
    private var isDuplicateScreenStarted = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUIUpdated()
        startFirstOpenFlow()
    }

    private fun startFirstOpenFlow() {
        Log.i("TestActivity", "splash_scr")
        firstOpenFlowAdIds.apply {
            this["ADMOB_SPLASH_INTERSTITIAL"] = "ca-app-pub-3940256099942544/1033173712"
            this["ADMOB_SPLASH_RESUME"] = "ca-app-pub-3940256099942544/9257395921"
            this["ADMOB_BANNER_SPLASH"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_LANGUAGE_1"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_LANGUAGE_2"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_SURVEY_1"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_SURVEY_2"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_WALKTHROUGH_1"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_WALKTHROUGH_2"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_WALKTHROUGH_FULLSCR"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_NATIVE_WALKTHROUGH_3"] = "ca-app-pub-3940256099942544/2247696110"
            this["ADMOB_INTERSTITIAL_LETS_START"] = "ca-app-pub-3940256099942544/1033173712"

        }

        FOFAdsManager.setOnFlowStateListener(
            reConfigureBuilders = {
                FOFAdsManager.refreshStrings(
                    setUpWelcomeScreen(this),
                    getWalkThroughList(this)
                )
            },
            onFinish = {
                gotoMainActivity()
            }
        )

        val consentConfig = ConsentConfigurations.Builder()
            .setApplicationContext(application)
            .setMintegralInitializationId(
                appId = "144002",
                appKey = "7c22942b749fe6a6e361b675e96b3ee9"
            )
            .setUnityInitializationId(gameId = "1234567", testMode = true)
            .setActivityContext(this)
            .setTestDeviceHashedIdList(
                arrayListOf(
                    "09DD12A6DB3BBF9B55D65FAA9FD7D8E0",
                    "3F8FB4EE64D851EDBA704E705EC63A62",
                    "84C3994693FB491110A5A4AEF8C5561B",
                    "CB2F3812ACAA2A3D8C0B31682E1473EB",
                    "F02B044F22C917805C3DF6E99D3B8800"
                )
            )
            .setOnConsentGatheredCallback {
                Log.i("ConsentMessage", "StartActivity: setOnConsentGatheredCallback")
                fetchAdIDS(
                    remoteConfigOperationsCompleted = {
                        fofAdsConfigurations.setRemoteConfigData(
                            activityContext = this@TestActivity,
                            myRemoteConfigData = it
                        )

                        if (NetworkCheck.isNetworkAvailable(this)) {
                            if (it.getValue(RemoteConfigConstTest.BANNER_SPLASH) == true) {
                                binding.bannerAd.visibility = View.VISIBLE
                                loadAdmobBannerAd()
                            }
                        }
                    }
                )
            }
            .build()

        val welcomeScreensConfiguration = WelcomeScreensConfiguration.Builder()
            .setActivityContext(this)
            .setXMLLayout(setUpWelcomeScreen(this))
            .build()

        val languageScreensConfiguration = LanguageScreensConfiguration.Builder()
            .setActivityContext(this)
            .setDrawableColors(
                selectedDrawable = AppCompatResources.getDrawable(
                    this,
                    com.niceapps.fofscr.lib.R.drawable.ad_att_bg
                )!!,
                unSelectedDrawable = AppCompatResources.getDrawable(
                    this,
                    com.niceapps.fofscr.lib.R.drawable.ad_att_bg
                )!!,
                selectedRadio = AppCompatResources.getDrawable(
                    this,
                    com.niceapps.fofscr.lib.R.drawable.ic_radio_button_checked
                )!!,
                unSelectedRadio = AppCompatResources.getDrawable(
                    this,
                    com.niceapps.fofscr.lib.R.drawable.ic_radio_button_unchecked
                )!!,
                tickSelector = AppCompatResources.getDrawable(
                    this,
                    com.niceapps.fofscr.lib.R.drawable.ic_done
                )!!,
                themeColor = ContextCompat.getColor(this, R.color.red),
                statusBarColor = ContextCompat.getColor(this, R.color.red),
                font = ContextCompat.getColor(this, R.color.yellow),
                headingColor = ContextCompat.getColor(this, R.color.yellow),
            )
            .setEventTracker(FirebaseCommonTracker())
            .setLanguages(
                arrayListOf(
                    Language.Urdu,
                    Language.English,
                    Language.Hindi,
                    Language.French,
                    Language.Dutch,
                    Language.Arabic,
                    Language.German
                )
            )
            .build()

        val walkThroughScreensConfiguration = WalkThroughScreensConfiguration.Builder()
            .setActivityContext(this)
            .setWalkThroughContent(getWalkThroughList(this))
            .setEventTracker(FirebaseCommonTracker())
            .build()

        fofAdsConfigurations = FOFAdsConfigurations.Builder()
            .setFirstOpenFlowAdIds(firstOpenFlowAdIds)
            .setConsentConfig(consentConfig)
            .setLanguageScreenConfiguration(languageScreensConfiguration)
            .setWelcomeScreenConfiguration(welcomeScreensConfiguration)
            .setWalkThroughScreenConfiguration(walkThroughScreensConfiguration)
            .build()

        FOFAdsManager.startFlow(fofAdsConfigurations)
    }





    private fun loadAdmobBannerAd() {
        AdMobBannerAdSplash(
            activity = this@TestActivity,
            placementID = "ca-app-pub-3940256099942544/9214589741",
            bannerContainer = binding.bannerAd,
            shimmerContainer = binding.bannerShimmerLayout.root,
            onAdFailed = {
                binding.bannerAd.visibility = View.GONE
            },
            onAdLoaded = {
            },
            onAdClicked = {}
        )
    }



    private fun gotoMainActivity() {
        val time = if (PrefHelper(this).getBooleanDefault("StartScreens", default = false)) {
            0
        } else {
            if (NetworkCheck.isNetworkAvailable(this)) {
                0
            } else {
                3000
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, FinalActivity::class.java)
            startActivity(intent)
            finish()
        }, time.toLong())
    }

    private fun setUpWelcomeScreen(context: Context): View {
        val localizedConfig =
            resources.configuration.apply { MyLocaleHelper.onAttach(context, "en") }
        val localizedContext = ContextWrapper(context).createConfigurationContext(localizedConfig)

        val welcomeScreenView = LayoutInflater.from(localizedContext)
            .inflate(R.layout.layout_welcome_scr_test, null, false)

        val txtWallpapers = welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtWallpapers)
        val txtEditor = welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtEditor)
        val txtLiveThemes = welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtLiveThemes)
        val txtPhotoOnKeyboard =
            welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtPhotoOnKeyboard)
        val txtPhotoTranslator =
            welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtPhotoTranslator)
        val txtInstantSticker =
            welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtInstantSticker)
        val txtLiveTranslator =
            welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtLiveTranslator)
        val txtUrduSticker = welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtUrduSticker)

        var txtWallpapersBool = false
        var txtEditorBool = false
        var txtLiveThemesBool = false
        var txtPhotoOnKeyboardBool = false
        var txtPhotoTranslatorBool = false
        var txtInstantStickerBool = false
        var txtLiveTranslatorBool = false
        var txtUrduStickerBool = false

        val nextButton = welcomeScreenView.findViewById<AppCompatTextView>(R.id.txtNext)

        txtWallpapers.setOnClickListener {
            Log.i("TestActivity", "survey_scr_check_wallpaper")


            if (isDuplicateScreenStarted) {
                FOFAdsManager.showWelcomeDupScreen()
            }

            isDuplicateScreenStarted = false
            if (txtWallpapersBool) {
                txtWallpapersBool = false
            } else {
                txtWallpapersBool = true
            }
        }
        txtEditor.setOnClickListener {
            Log.i("TestActivity", "survey_scr_check_urdu_editor")
            if (isDuplicateScreenStarted) {
                FOFAdsManager.showWelcomeDupScreen()
            }
            isDuplicateScreenStarted = false
            if (txtEditorBool) {
                txtEditorBool = false
            } else {
                txtEditorBool = true
            }
        }
        txtLiveThemes.setOnClickListener {
            Log.i("TestActivity", "survey_scr_check_live_themes")
            if (isDuplicateScreenStarted) {
                FOFAdsManager.showWelcomeDupScreen()
            }
            isDuplicateScreenStarted = false
            if (txtLiveThemesBool) {
                txtLiveThemesBool = false
            } else {
                txtLiveThemesBool = true
            }
        }
        txtPhotoOnKeyboard.setOnClickListener {
            Log.i("TestActivity", "survey_scr_check_photo_on_keyboard")
            if (isDuplicateScreenStarted) {
                FOFAdsManager.showWelcomeDupScreen()
            }
            isDuplicateScreenStarted = false
            if (txtPhotoOnKeyboardBool) {
                txtPhotoOnKeyboardBool = false
            } else {
                txtPhotoOnKeyboardBool = true
            }
        }
        txtPhotoTranslator.setOnClickListener {
            Log.i("TestActivity", "survey_scr_check_photo_translator")
            if (isDuplicateScreenStarted) {
                FOFAdsManager.showWelcomeDupScreen()
            }
            isDuplicateScreenStarted = false
            if (txtPhotoTranslatorBool) {
                txtPhotoTranslatorBool = false
            } else {
                txtPhotoTranslatorBool = true
            }
        }
        txtInstantSticker.setOnClickListener {
            Log.i("TestActivity", "survey_scr_check_instant_stickers")
            if (isDuplicateScreenStarted) {
                FOFAdsManager.showWelcomeDupScreen()
            }
            isDuplicateScreenStarted = false
            if (txtInstantStickerBool) {
                txtInstantStickerBool = false
            } else {
                txtInstantStickerBool = true
            }
        }
        txtLiveTranslator.setOnClickListener {
            Log.i("TestActivity", "survey_scr_check_live_translator")
            if (isDuplicateScreenStarted) {
                FOFAdsManager.showWelcomeDupScreen()
            }
            isDuplicateScreenStarted = false
            if (txtLiveTranslatorBool) {
                txtLiveTranslatorBool = false
            } else {
                txtLiveTranslatorBool = true
            }
        }
        txtUrduSticker.setOnClickListener {
            Log.i("TestActivity", "survey_scr_check_urdu_stickers")
            if (isDuplicateScreenStarted) {
                FOFAdsManager.showWelcomeDupScreen()
            }
            isDuplicateScreenStarted = false
            if (txtUrduStickerBool) {
                txtUrduStickerBool = false
            } else {
                txtUrduStickerBool = true
            }
        }

        nextButton.setOnClickListener {
            if (txtWallpapersBool || txtEditorBool ||
                txtLiveThemesBool || txtPhotoOnKeyboardBool ||
                txtPhotoTranslatorBool || txtInstantStickerBool ||
                txtLiveTranslatorBool || txtUrduStickerBool
            ) {
                Log.i("TestActivity", "survey2_scr")
                Log.i("TestActivity", "survey2_scr_tap_continue")
                FOFAdsManager.completeWelcomeScreens()
            } else {
                Log.i("TestActivity", "survey1_scr")
                Log.i("TestActivity", "survey1_scr_tap_continue")
                if (isDuplicateScreenStarted) {
                    FOFAdsManager.showWelcomeDupScreen()
                }
                isDuplicateScreenStarted = false
                val toast = Toast.makeText(context, "Please check the checkbox", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }
        return welcomeScreenView
    }





    private fun getWalkThroughList(context: Context): ArrayList<WalkThroughItem> {
        val localizedContext = ContextWrapper(context).createConfigurationContext(
            resources.configuration.apply { MyLocaleHelper.onAttach(context, "en") }
        )
        return arrayListOf(
            WalkThroughItem(
                heading = "Screen 1",
                description = "This is screen one",
                headingColor = com.niceapps.fofscr.lib.R.color.redLib,
                descriptionColor = com.niceapps.fofscr.lib.R.color.yellowLib,
                nextColor = com.niceapps.fofscr.lib.R.color.orangeLib,
                drawableResId = com.niceapps.fofscr.lib.R.drawable.pakistan,
                drawableBubbleResId = R.drawable.ic_launcher_foreground,
                viewBackgroundColor = R.color.red,
                imageScale = 1
            ),
            WalkThroughItem(
                heading = "Screen 2",
                description = "This is screen two",
                headingColor = com.niceapps.fofscr.lib.R.color.redLib,
                descriptionColor = com.niceapps.fofscr.lib.R.color.yellowLib,
                nextColor = com.niceapps.fofscr.lib.R.color.orangeLib,
                drawableResId = com.niceapps.fofscr.lib.R.drawable.img2,
                drawableBubbleResId = R.drawable.ic_launcher_foreground,
                viewBackgroundColor = R.color.red,
                imageScale = 1
            ),
            WalkThroughItem(
                heading = "Screen 3",
                description = "This is screen three",
                headingColor = com.niceapps.fofscr.lib.R.color.redLib,
                descriptionColor = com.niceapps.fofscr.lib.R.color.yellowLib,
                nextColor = com.niceapps.fofscr.lib.R.color.orangeLib,
                drawableResId = com.niceapps.fofscr.lib.R.drawable.img2,
                drawableBubbleResId = R.drawable.ic_launcher_foreground,
                viewBackgroundColor = R.color.red,
                imageScale = 1
            )
        )
    }



    private fun fetchAdIDS(remoteConfigOperationsCompleted: (HashMap<String, Any>) -> Unit) {
        if (NetworkCheck.isNetworkAvailable(this@TestActivity)) {
            saveAllValues()
            remoteConfigOperationsCompleted.invoke(getSharedPreferencesValues())
        } else {
            remoteConfigOperationsCompleted.invoke(getSharedPreferencesValues())
        }
    }

    private fun saveAllValues() {
        val editor = getSharedPreferences("RemoteConfig", MODE_PRIVATE).edit()
        // Ads-Visibility-Config
        editor.putString(RemoteConfigConstTest.RESUME_INTER_SPLASH, "INTERSTITIAL")
        editor.putBoolean(RemoteConfigConstTest.BANNER_SPLASH, true)
        editor.putBoolean(RemoteConfigConstTest.RESUME_OVERALL, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_LANGUAGE_1, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_LANGUAGE_2, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_SURVEY_1, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_SURVEY_2, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_1, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_2, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_FULLSCR, true)
        editor.putBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_3, true)
        editor.putBoolean(RemoteConfigConstTest.INTERSTITIAL_LETS_START, true)
        editor.putString(RemoteConfigConstTest.TIMER_NATIVE_F_SRC, "5")

        editor.apply()
    }

    private fun getSharedPreferencesValues(): HashMap<String, Any> {
        val remoteConfigHashMap: HashMap<String, Any> = HashMap()
        val prefs: SharedPreferences = getSharedPreferences("RemoteConfig", Context.MODE_PRIVATE)

        remoteConfigHashMap.apply {
            this["RESUME_INTER_SPLASH"] =
                "${prefs.getString(RemoteConfigConstTest.RESUME_INTER_SPLASH, "Empty")}"
            this["BANNER_SPLASH"] = prefs.getBoolean(RemoteConfigConstTest.BANNER_SPLASH, false)
            this["RESUME_OVERALL"] = prefs.getBoolean(RemoteConfigConstTest.RESUME_OVERALL, false)
            this["NATIVE_LANGUAGE_1"] =
                prefs.getBoolean(RemoteConfigConstTest.NATIVE_LANGUAGE_1, false)
            this["NATIVE_LANGUAGE_2"] =
                prefs.getBoolean(RemoteConfigConstTest.NATIVE_LANGUAGE_2, false)
            this["NATIVE_SURVEY_1"] = prefs.getBoolean(RemoteConfigConstTest.NATIVE_SURVEY_1, false)
            this["NATIVE_SURVEY_2"] = prefs.getBoolean(RemoteConfigConstTest.NATIVE_SURVEY_2, false)
            this["NATIVE_WALKTHROUGH_1"] =
                prefs.getBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_1, false)
            this["NATIVE_WALKTHROUGH_2"] =
                prefs.getBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_2, false)
            this["NATIVE_WALKTHROUGH_FULLSCR"] =
                prefs.getBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_FULLSCR, false)
            this["NATIVE_WALKTHROUGH_3"] =
                prefs.getBoolean(RemoteConfigConstTest.NATIVE_WALKTHROUGH_3, false)
            this["INTERSTITIAL_LETS_START"] =
                prefs.getBoolean(RemoteConfigConstTest.INTERSTITIAL_LETS_START, false)


            this["TIMER_NATIVE_F_SRC"] =
                "${prefs.getString(RemoteConfigConstTest.TIMER_NATIVE_F_SRC, "Empty")}"
        }
        return remoteConfigHashMap
    }
}