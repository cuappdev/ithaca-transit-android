package ithaca_transit.android.cornellappdev.com.ithaca_transit.Utils

import android.app.Application
import android.content.Context

class App : Application() {

    val Context.app : App
        get() = applicationContext as App

}
