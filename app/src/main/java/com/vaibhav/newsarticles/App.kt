package com.vaibhav.newsarticles

import android.app.Application
import com.google.android.material.color.DynamicColors

class App :Application(){
    override fun onCreate() {
        super.onCreate()

        // Apply dynamics colors according to your device colors. it works only Above Android Version 12.
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}