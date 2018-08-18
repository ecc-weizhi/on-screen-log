package app.eccweizhi.onscreenlog.example

import android.app.Application
import app.eccweizhi.onscreenlog.OnScreenLog


class MyApplication : Application() {
    lateinit var onScreenLog: OnScreenLog
        private set

    override fun onCreate() {
        super.onCreate()
        onScreenLog = OnScreenLog.builder()
                .context(this)
                .notificationId(1)
                .build()
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE: MyApplication
    }
}