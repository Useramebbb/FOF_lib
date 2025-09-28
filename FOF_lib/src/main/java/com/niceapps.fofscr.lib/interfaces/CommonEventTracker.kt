package com.niceapps.fofscr.lib.interfaces

import android.app.Activity
import android.content.Context

interface CommonEventTracker {
    fun logEvent(
        context: Activity,
        eventName: String,
        params: Map<String, Any> = emptyMap()
    )
}