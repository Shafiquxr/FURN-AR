package com.spatialshop.ai.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Spatial Shopping AI.
 * Annotated with @HiltAndroidApp to trigger Hilt's code generation.
 */
@HiltAndroidApp
class SpatialShopApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize any app-level components here
    }
}
