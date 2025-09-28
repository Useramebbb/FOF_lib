package com.niceapps.fofscr.lib.callingClasses

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import com.manual.niceapps.fofscr.lib.activities.LanguageScreenOne
import com.niceapps.fofscr.lib.data.Language
import com.niceapps.fofscr.lib.interfaces.CommonEventTracker
import com.niceapps.fofscr.lib.interfaces.LanguageInterface

class LanguageScreensConfiguration private constructor() {

    private lateinit var activityContext: Activity
    private var languageInterface: LanguageInterface? = null
    var languageList: ArrayList<Language>? = null
    var eventTracker: CommonEventTracker? = null
    var selectedDrawable: Drawable? = null
    var unSelectedDrawable: Drawable? = null
    var selectedRadio: Drawable? = null
    var tickSelector: Drawable? = null
    var unSelectedRadio: Drawable? = null
     var theme: Int? = null
     var statusBarColor: Int? = null
     var fontColor:Int? = null
     var headingColor:Int? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        var languageInstance: LanguageScreensConfiguration? = null
    }

    fun languageInitializationSetup() {
        Log.i("LanguageScreensConfiguration", "Language: languageInitializationSetup()")
        activityContext.startActivity(Intent(activityContext, LanguageScreenOne::class.java))
        activityContext.finish()
    }

    fun setLanguageInterface(languageInterface: LanguageInterface?) {
        this.languageInterface = languageInterface
    }

    fun showLanguageTwoScreen() {
        Log.i("StartTestActivity", "language1_scr_tap_language")
        languageInterface?.showLanguageTwoScreen()
    }

    class Builder {
        private lateinit var activity: Activity
        private var languageList: ArrayList<Language>? = null
        private var selectedDrawable: Drawable? = null
        private var unSelectedDrawable: Drawable? = null
        private var selectedRadio: Drawable? = null
        private var tickSelector: Drawable? = null
        private var unSelectedRadio: Drawable? = null
        private var theme: Int? = null
        private var statusBarColor: Int? = null
        private var fontColor:Int? = null
        private var headingColor:Int? = null
        private var eventTracker: CommonEventTracker? = null

        fun setActivityContext(myActivity: Activity) = apply {
            this.activity = myActivity
        }
        fun setEventTracker(tracker: CommonEventTracker) = apply { this.eventTracker = tracker }
        fun setDrawableColors(selectedDrawable: Drawable, unSelectedDrawable: Drawable, selectedRadio: Drawable, unSelectedRadio: Drawable,tickSelector:Drawable,themeColor:Int,statusBarColor:Int,font: Int,headingColor:Int) = apply {
            this.selectedDrawable = selectedDrawable
            this.unSelectedDrawable = unSelectedDrawable
            this.selectedRadio = selectedRadio
            this.unSelectedRadio = unSelectedRadio
            this.tickSelector = tickSelector
            this.fontColor = font
            this.theme = themeColor
            this.statusBarColor = statusBarColor
            this.headingColor = headingColor
        }

        fun setLanguages(languageList: ArrayList<Language>) = apply {
            this.languageList = languageList
        }

        fun build(): LanguageScreensConfiguration {
            if (!::activity.isInitialized) {
                throw IllegalStateException("Activity context must be provided")
            }
            val languageScreensConfiguration = LanguageScreensConfiguration()
            languageScreensConfiguration.activityContext = activity
            languageScreensConfiguration.languageList = languageList!!
            languageScreensConfiguration.selectedDrawable = selectedDrawable!!
            languageScreensConfiguration.unSelectedDrawable = unSelectedDrawable!!
            languageScreensConfiguration.selectedRadio = selectedRadio!!
            languageScreensConfiguration.unSelectedRadio = unSelectedRadio!!
            languageScreensConfiguration.theme = theme!!
            languageScreensConfiguration.statusBarColor = statusBarColor!!
            languageScreensConfiguration.fontColor = fontColor!!
            languageScreensConfiguration.headingColor = headingColor!!
            languageScreensConfiguration.tickSelector = tickSelector!!
            languageScreensConfiguration.eventTracker = eventTracker
            languageInstance = languageScreensConfiguration
            return languageScreensConfiguration
        }
    }
}