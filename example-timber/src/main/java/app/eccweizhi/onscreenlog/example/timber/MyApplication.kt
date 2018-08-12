package app.eccweizhi.onscreenlog.example.timber

import android.app.Application
import app.eccweizhi.onscreenlog.OnScreenLog
import app.eccweizhi.onscreenlog.timber.OnScreenLoggingTree
import timber.log.Timber


class MyApplication : Application() {
    val onScreenLog = OnScreenLog()

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        Timber.plant(OnScreenLoggingTree(onScreenLog))
    }

    companion object {
        lateinit var INSTANCE: MyApplication
    }
}