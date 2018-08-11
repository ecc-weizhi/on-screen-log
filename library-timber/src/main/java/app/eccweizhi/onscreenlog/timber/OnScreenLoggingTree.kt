package app.eccweizhi.onscreenlog.timber

import android.util.Log
import app.eccweizhi.onscreenlog.OnScreenLog
import timber.log.Timber


class OnScreenLoggingTree(private val onScreenLog: OnScreenLog) : Timber.DebugTree() {
    private fun logInternal(priority: Int,
                            tag: String,
                            message: String) {
        when (priority) {
            Log.VERBOSE -> {
                onScreenLog.v(tag,
                        message)
            }
            Log.DEBUG -> {
                onScreenLog.d(tag,
                        message)
            }
            Log.INFO -> {
                onScreenLog.i(tag,
                        message)
            }
            Log.WARN -> {
                onScreenLog.w(tag,
                        message)
            }
            Log.ERROR -> {
                onScreenLog.e(tag,
                        message)
            }
            Log.ASSERT -> {
                onScreenLog.wtf(tag,
                        message)
            }
        }
    }

    override fun log(priority: Int,
                     tag: String?,
                     message: String,
                     t: Throwable?) {
        if (message.length < MAX_LOG_LENGTH) {
            logInternal(priority,
                    tag ?: "",
                    message)
            return
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        var i = 0
        val length = message.length
        while (i < length) {
            var newline = message.indexOf('\n',
                    i)
            newline = if (newline != -1) newline else length
            do {
                val end = Math.min(newline,
                        i + MAX_LOG_LENGTH)
                val part = message.substring(i,
                        end)
                logInternal(priority,
                        tag ?: "",
                        part)
                i = end
            } while (i < newline)
            i++
        }
    }

    companion object {
        private const val MAX_LOG_LENGTH = 4000
    }
}