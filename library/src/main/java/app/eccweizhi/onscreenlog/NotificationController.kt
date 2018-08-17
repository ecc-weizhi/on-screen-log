package app.eccweizhi.onscreenlog

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit


internal class NotificationController(private val context: Context,
                                      private val notificationId: Int,
                                      subject: BehaviorSubject<List<Message>>) {
    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        createNotificationChannel()
        subject.sample(SAMPLE_DURATION_MS, TimeUnit.MILLISECONDS, true)
                .subscribe { newList: List<Message> ->
                    if (newList.isEmpty()) {
                        val notification = buildEmptyNotification()
                        notificationManager.notify(notificationId, notification)
                    } else {
                        val notification = buildNotification(newList)
                        notificationManager.notify(notificationId, notification)
                    }
                }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW)
            channel.description = CHANNEL_DESCRIPTION

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getNotificationContentIntent(): PendingIntent {
        val intent = Intent(context, LogActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    private fun buildEmptyNotification(): Notification {
        return NotificationCompat.Builder(context,
                CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(CONTENT_TITLE)
                .setContentText(EMPTY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setContentIntent(getNotificationContentIntent())
                .build()
    }

    /**
     * @param messageList must not be empty
     */
    private fun buildNotification(messageList: List<Message>): Notification {
        return NotificationCompat.Builder(context,
                CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(CONTENT_TITLE)
                .setContentText(messageList.last().content)
                .setStyle(buildInboxStyleNotification(messageList))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setContentIntent(getNotificationContentIntent())
                .build()
    }

    /**
     * @param messageList must not be empty
     */
    private fun buildInboxStyleNotification(messageList: List<Message>): NotificationCompat.InboxStyle {
        val style = NotificationCompat.InboxStyle()
        val lastIndex = messageList.lastIndex
        for (i in 0 until NOTIFICATION_MAX_MESSAGE) {
            val currentIndex = lastIndex - i
            if (currentIndex < 0) {
                break
            }
            style.addLine(messageList[currentIndex].content)
        }
        return style
    }

    companion object {
        private const val CHANNEL_ID = "onScreenLog"        // not user visible
        private const val CHANNEL_NAME = "on-screen-log"    // user visible
        private const val CHANNEL_DESCRIPTION = "Notification for on-screen-log"
        private const val CONTENT_TITLE = "Displaying logs"
        private const val NOTIFICATION_MAX_MESSAGE = 10
        private const val EMPTY_MESSAGE = "No logs yet"
        private const val SAMPLE_DURATION_MS = 200L
    }
}