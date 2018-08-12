package app.eccweizhi.onscreenlog

import android.util.Log
import io.reactivex.subjects.BehaviorSubject
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import java.util.concurrent.atomic.AtomicLong


class OnScreenLog(private val capacity: Int = 100,
                  private val outputToLogcat: Boolean = true) {
    val subject: BehaviorSubject<List<Message>> = BehaviorSubject.createDefault(listOf())
    val adapter = LogAdapter.newInstance(subject)

    private val list = LinkedList<Message>()
    private val idCounter = AtomicLong(0)

    init {
        if (capacity <= 0) throw IllegalArgumentException("Capacity must be more than 0")
    }

    fun v(tag: String,
          message: String,
          vararg objects: Any) {
        log(Log.VERBOSE,
                tag,
                null,
                formatMessage(message,
                        *objects))
    }

    fun v(tag: String,
          t: Throwable,
          message: String,
          vararg objects: Any) {
        val formattedMessage = formatMessage(message,
                *objects)
        val finalMessage = "$formattedMessage\n${getStackTraceString(t)}"

        log(Log.VERBOSE,
                tag,
                t,
                finalMessage)
    }

    fun v(tag: String,
          t: Throwable) {
        log(Log.VERBOSE,
                tag,
                t,
                getStackTraceString(t))
    }

    fun d(tag: String,
          message: String,
          vararg objects: Any) {
        log(Log.DEBUG,
                tag,
                null,
                formatMessage(message,
                        *objects))
    }

    fun d(tag: String,
          t: Throwable,
          message: String,
          vararg objects: Any) {
        val formattedMessage = formatMessage(message,
                *objects)
        val finalMessage = "$formattedMessage\n${getStackTraceString(t)}"

        log(Log.DEBUG,
                tag,
                t,
                finalMessage)
    }

    fun d(tag: String,
          t: Throwable) {
        log(Log.DEBUG,
                tag,
                t,
                getStackTraceString(t))
    }

    fun i(tag: String,
          message: String,
          vararg objects: Any) {
        log(Log.INFO,
                tag,
                null,
                formatMessage(message,
                        *objects))
    }

    fun i(tag: String,
          t: Throwable,
          message: String,
          vararg objects: Any) {
        val formattedMessage = formatMessage(message,
                *objects)
        val finalMessage = "$formattedMessage\n${getStackTraceString(t)}"

        log(Log.INFO,
                tag,
                t,
                finalMessage)
    }

    fun i(tag: String,
          t: Throwable) {
        log(Log.INFO,
                tag,
                t,
                getStackTraceString(t))
    }

    fun w(tag: String,
          message: String,
          vararg objects: Any) {
        log(Log.WARN,
                tag,
                null,
                formatMessage(message,
                        *objects))
    }

    fun w(tag: String,
          t: Throwable,
          message: String,
          vararg objects: Any) {
        val formattedMessage = formatMessage(message,
                *objects)
        val finalMessage = "$formattedMessage\n${getStackTraceString(t)}"

        log(Log.WARN,
                tag,
                t,
                finalMessage)
    }

    fun w(tag: String,
          t: Throwable) {
        log(Log.WARN,
                tag,
                t,
                getStackTraceString(t))
    }

    fun e(tag: String,
          message: String,
          vararg objects: Any) {
        log(Log.ERROR,
                tag,
                null,
                formatMessage(message,
                        *objects))
    }

    fun e(tag: String,
          t: Throwable,
          message: String,
          vararg objects: Any) {
        val formattedMessage = formatMessage(message,
                *objects)
        val finalMessage = "$formattedMessage\n${getStackTraceString(t)}"

        log(Log.ERROR,
                tag,
                t,
                finalMessage)
    }

    fun e(tag: String,
          t: Throwable) {
        log(Log.ERROR,
                tag,
                t,
                getStackTraceString(t))
    }

    fun wtf(tag: String,
            message: String,
            vararg objects: Any) {
        log(Log.ASSERT,
                tag,
                null,
                formatMessage(message,
                        *objects))
    }

    fun wtf(tag: String,
            t: Throwable,
            message: String,
            vararg objects: Any) {
        val formattedMessage = formatMessage(message,
                *objects)
        val finalMessage = "$formattedMessage\n${getStackTraceString(t)}"

        log(Log.ASSERT,
                tag,
                t,
                finalMessage)
    }

    fun wtf(tag: String,
            t: Throwable) {
        log(Log.ASSERT,
                tag,
                t,
                getStackTraceString(t))
    }

    private fun formatMessage(message: String,
                              vararg args: Any): String {
        return String.format(message,
                *args)
    }

    private fun getStackTraceString(t: Throwable): String {
        // Don't replace this with Log.getStackTraceString() - it hides
        // UnknownHostException, which is not what we want.
        val sw = StringWriter(256)
        val pw = PrintWriter(sw,
                false)
        t.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }

    private fun log(priority: Int,
                    tag: String,
                    t: Throwable?,
                    finalMessage: String) {
        val size = list.size

        list.offerLast(Message(idCounter.incrementAndGet(),
                priority,
                tag,
                finalMessage))
        if (size == capacity + 1) {
            list.pollFirst()
        }

        val clone = list.clone() as List<Message>
        subject.onNext(clone)

        if (outputToLogcat) {
            Log.println(priority,
                    tag,
                    finalMessage)
        }
    }
}