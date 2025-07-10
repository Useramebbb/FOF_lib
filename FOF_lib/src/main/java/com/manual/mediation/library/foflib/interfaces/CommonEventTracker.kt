package com.manual.mediation.library.foflib.interfaces

import android.app.Activity

interface CommonEventTracker {
    fun logEvent(
        context: Activity,
        eventName: String,
        params: Map<String, Any> = emptyMap()
    )
}