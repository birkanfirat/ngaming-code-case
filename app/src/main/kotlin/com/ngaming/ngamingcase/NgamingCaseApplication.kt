package com.ngaming.ngamingcase

import android.app.Application
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp

/** Uygulamanın giriş noktası. Hilt'in bağımlılık grafiği buradan başlıyor. */
@HiltAndroidApp
class NgamingCaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
