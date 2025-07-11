package com.manual.mediation.library.sotadlib.interfaces

import android.app.Activity
import android.content.Context

interface CommonEventTracker {
    fun logEvent(
        context: Activity,
        eventName: String,
        params: Map<String, Any> = emptyMap()
    )
}