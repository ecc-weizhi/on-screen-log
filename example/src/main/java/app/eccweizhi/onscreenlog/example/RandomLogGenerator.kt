package app.eccweizhi.onscreenlog.example

import app.eccweizhi.onscreenlog.OnScreenLog
import java.util.*


class RandomLogGenerator(private val onScreenLog: OnScreenLog) {
    private val random = Random()

    fun logRandomMessage() {
        val priority = random.nextInt(6)

        when (priority) {
            0 -> onScreenLog.v(javaClass.simpleName,
                    "Something happened %s",
                    UUID.randomUUID().toString())
            1 -> onScreenLog.d(javaClass.simpleName,
                    "Something happened %s",
                    UUID.randomUUID().toString())
            2 -> onScreenLog.i(javaClass.simpleName,
                    "Something happened %s",
                    UUID.randomUUID().toString())
            3 -> onScreenLog.w(javaClass.simpleName,
                    "Something happened %s",
                    UUID.randomUUID().toString())
            4 -> onScreenLog.e(javaClass.simpleName,
                    "Something happened %s",
                    UUID.randomUUID().toString())
            5 -> onScreenLog.wtf(javaClass.simpleName,
                    "Something happened %s",
                    UUID.randomUUID().toString())
        }
    }
}