package com.example

import android.content.Context
import org.mozilla.geckoview.GeckoRuntime

object GeckoEngine {
    private var runtime: GeckoRuntime? = null

    fun getRuntime(context: Context): GeckoRuntime {
        synchronized(this) {
            if (runtime == null) {
                runtime = GeckoRuntime.getDefault(context.applicationContext)
            }
            return runtime!!
        }
    }
}
