package app.eccweizhi.onscreenlog.example.timber

import android.app.Application
import app.eccweizhi.onscreenlog.OnScreenLog
import app.eccweizhi.onscreenlog.timber.OnScreenLoggingTree
import timber.log.Timber


class MyApplication : Application() {
    lateinit var onScreenLog: OnScreenLog
        private set

    override fun onCreate() {
        super.onCreate()
        onScreenLog = OnScreenLog.builder()
                .context(this)
                .build()
        INSTANCE = this

        Timber.plant(OnScreenLoggingTree(onScreenLog))
    }

    companion object {
        lateinit var INSTANCE: MyApplication
    }
}