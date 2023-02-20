package fi.dy.ose.mobilecomputing

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import fi.dy.ose.mobilecomputing.ui.Graph

@HiltAndroidApp
class MobileCompApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}