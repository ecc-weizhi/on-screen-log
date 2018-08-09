package app.eccweizhi.onscreenlog.example

import android.app.Application
import app.eccweizhi.onscreenlog.OnScreenLog


class MyApplication : Application() {
    val onScreenLog = OnScreenLog()

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE: MyApplication
    }
}