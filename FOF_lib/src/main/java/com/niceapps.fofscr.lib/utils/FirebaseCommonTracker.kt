package com.niceapps.fofscr.lib.utils

import android.app.Activity
import android.util.Log
import com.niceapps.fofscr.lib.interfaces.CommonEventTracker

class FirebaseCommonTracker : CommonEventTracker {
    override fun logEvent(context: Activity, eventName: String, params: Map<String, Any>) {
        Log.d("logEvent", "logEvent:$eventName ")
    }
}