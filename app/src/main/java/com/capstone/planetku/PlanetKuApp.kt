package com.capstone.planetku

import android.app.Application
import com.capstone.planetku.utils.ThemeHelper

class PlanetKuApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ThemeHelper.applyTheme(this)
    }
}
