package com.niceapps.fofscr.lib.callingClasses

import android.view.View
import com.niceapps.fofscr.lib.data.WalkThroughItem
import java.util.ArrayList

object FOFAdsManager {

    private var fofAdsConfigurations: FOFAdsConfigurations? = null
    private var onFinish: (() -> Unit)? = null
    private var reConfigureBuilders: (() -> Unit)? = null

    fun startFlow(fofAdsConfigurations: FOFAdsConfigurations) {
        if (this.fofAdsConfigurations == null) {
            this.fofAdsConfigurations = fofAdsConfigurations
        }
    }

    fun showWelcomeScreen() {
        fofAdsConfigurations?.startWelcomeScreenConfiguration()
    }

    fun showWelcomeDupScreen() {
        fofAdsConfigurations?.welcomeScreensConfiguration?.showWelcomeTwoScreen()
    }

    fun completeWelcomeScreens() {
        fofAdsConfigurations?.welcomeScreensConfiguration?.endWelcomeTwoScreen()
        fofAdsConfigurations?.startWalkThroughConfiguration()
    }

    fun getConfigurations(): FOFAdsConfigurations? {
        return fofAdsConfigurations
    }

    fun setOnFlowStateListener(reConfigureBuilders: () -> Unit, onFinish: () -> Unit) {
        this.onFinish = onFinish
        this.reConfigureBuilders = reConfigureBuilders
    }

    fun notifyFlowFinished() {
        onFinish?.invoke()
    }

    fun notifyReConfigureBuilders() {
        reConfigureBuilders?.invoke()
    }

    fun refreshStrings(upWelcomeScreen: View, walkThroughList: ArrayList<WalkThroughItem>) {
        fofAdsConfigurations?.welcomeScreensConfiguration?.view = upWelcomeScreen
        fofAdsConfigurations?.walkThroughScreensConfiguration?.walkThroughList?.clear()
        fofAdsConfigurations?.walkThroughScreensConfiguration?.walkThroughList = walkThroughList
    }
}
