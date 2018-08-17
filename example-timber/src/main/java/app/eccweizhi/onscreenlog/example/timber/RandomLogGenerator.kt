package app.eccweizhi.onscreenlog.example.timber

import timber.log.Timber
import java.util.*


class RandomLogGenerator {
    private val random = Random()

    fun logRandomMessage() {
        val priority = random.nextInt(6)

        when (priority) {
            0 -> Timber.v("Something happened %s",
                    UUID.randomUUID().toString())
            1 -> Timber.d("Something happened %s",
                    UUID.randomUUID().toString())
            2 -> Timber.i("Something happened %s",
                    UUID.randomUUID().toString())
            3 -> Timber.w("Something happened %s",
                    UUID.randomUUID().toString())
            4 -> Timber.e("Something happened %s",
                    UUID.randomUUID().toString())
            5 -> Timber.wtf("Something happened %s",
                    UUID.randomUUID().toString())
        }
    }
}