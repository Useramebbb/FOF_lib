package com.manual.mediation.library.sotadlib.utils

import android.app.Activity
import android.util.Log
import com.manual.mediation.library.sotadlib.interfaces.CommonEventTracker

class FirebaseCommonTracker : CommonEventTracker {
    override fun logEvent(context: Activity, eventName: String, params: Map<String, Any>) {
        Log.d("logEvent", "logEvent:$eventName ")
    }
}