package app.eccweizhi.onscreenlog

import android.content.Context
import android.util.Log
import io.reactivex.subjects.BehaviorSubject
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import java.util.concurrent.atomic.AtomicLong


/**
 * Entry point to OnScreenLog library.
 *
 * @param applicationContext
 * @param notificationId Id to uniquely identify notification.
 * @param capacity size of messages to keep in memory. Default is 100.
 * @param outputToLogcat display logging via [OnScreenLog] in logcat if true
 * @property adapter reference to an instance of `RecyclerView.Adapter` containing logging messages
 */
class OnScreenLog private constructor(private val applicationContext: Context,
                                      notificationId: Int,
                                      private val capacity: Int,
                                      private val outputToLogcat: Boolean) {
    private val list = LinkedList<Message>()
    private val idCounter = AtomicLong(0)

    private val subject: BehaviorSubject<List<Message>> = BehaviorSubject.createDefault(listOf())
    private val notificationController = NotificationController(applicationContext,
            notificationId,
            subject)

    val adapter: LogAdapter by lazy { LogAdapter(subject) }

    fun v(tag: String,
          message: String,
          vararg objects: Any) {
        log(Log.VERBOSE,
                tag,
                null,
                formatMessage(message, *objects))
    }

    fun v(tag: String,
          t: Throwable,
          message: String,
          vararg objects: Any) {
        val formattedMessage = formatMessage(message, *objects)
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
                formatMessage(message, *objects))
    }

    fun d(tag: String,
          t: Throwable,
          message: String,
          vararg objects: Any) {
        val formattedMessage = formatMessage(message, *objects)
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
                formatMessage(message, *objects))
    }

    fun i(tag: String,
          t: Throwable,
          message: String,
          vararg objects: Any) {
        val formattedMessage = formatMessage(message, *objects)
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
                formatMessage(message, *objects))
    }

    fun w(tag: String,
          t: Throwable,
          message: String,
          vararg objects: Any) {
        val formattedMessage = formatMessage(message, *objects)
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
                formatMessage(message, *objects))
    }

    fun e(tag: String,
          t: Throwable,
          message: String,
          vararg objects: Any) {
        val formattedMessage = formatMessage(message, *objects)
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
                formatMessage(message, *objects))
    }

    fun wtf(tag: String,
            t: Throwable,
            message: String,
            vararg objects: Any) {
        val formattedMessage = formatMessage(message, *objects)
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
        return String.format(message, *args)
    }

    private fun getStackTraceString(t: Throwable): String {
        // Don't replace this with Log.getStackTraceString() - it hides
        // UnknownHostException, which is not what we want.
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
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

        val clone = ArrayList(list)
        subject.onNext(clone)

        if (outputToLogcat) {
            Log.println(priority, tag, finalMessage)
        }
    }

    companion object {
        @JvmStatic
        fun builder() = Builder()

        internal var INSTANCE: OnScreenLog? = null
    }

    class Builder internal constructor() {
        private var context: Context? = null
        private var capacity: Int = 100
        private var outputToLogcat: Boolean = true
        private var notificationId: Int? = null

        fun context(context: Context): Builder {
            this.context = context.applicationContext
            return this
        }

        fun notificationId(notificationId: Int): Builder {
            this.notificationId = notificationId
            return this
        }

        fun capacity(capacity: Int): Builder {
            if (capacity <= 0) throw IllegalArgumentException("Capacity must be more than 0")
            this.capacity = capacity
            return this
        }

        fun outputToLogcat(outputToLogcat: Boolean): Builder {
            this.outputToLogcat = outputToLogcat
            return this
        }

        @Synchronized
        fun build(): OnScreenLog {
            val tempContext = context
            val tempNotificationId = notificationId

            if (tempContext == null) {
                throw Exception("Must provide builder with context")
            }

            if (tempNotificationId == null) {
                throw Exception("Must provide builder with notificationId")
            }

            if (INSTANCE == null) {
                val onScreenLog = OnScreenLog(tempContext,
                        tempNotificationId,
                        capacity,
                        outputToLogcat)
                INSTANCE = onScreenLog
                return onScreenLog
            } else {
                throw Exception("OnScreenLog was already instantiated. Please reuse OnScreenLog instance.")
            }
        }
    }
}