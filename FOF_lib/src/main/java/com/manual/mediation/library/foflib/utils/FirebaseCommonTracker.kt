package com.manual.mediation.library.foflib.utils

import android.app.Activity
import android.util.Log
import com.manual.mediation.library.foflib.interfaces.CommonEventTracker

class FirebaseCommonTracker : CommonEventTracker {
    override fun logEvent(context: Activity, eventName: String, params: Map<String, Any>) {
        Log.d("logEvent", "logEvent:$eventName ")
    }
}