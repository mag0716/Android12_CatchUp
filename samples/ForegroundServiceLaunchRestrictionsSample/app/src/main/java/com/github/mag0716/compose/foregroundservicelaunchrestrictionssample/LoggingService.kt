package com.github.mag0716.compose.foregroundservicelaunchrestrictionssample

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class LoggingService : Service() {

    companion object {
        private const val TAG = "LoggingService"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "Foreground Service Channel"
        private const val KEY_DELAY = "Delay"
        fun createService(context: Context, delayMs: Long = 0L): Intent {
            return Intent(context, LoggingService::class.java).apply {
                putExtra(KEY_DELAY, delayMs)
            }
        }
    }

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val delayMs = intent?.getLongExtra(KEY_DELAY, 0L) ?: 0L

        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)

        coroutineScope.launch {
            outputLog(delayMs)
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun createNotificationChannel() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setSmallIcon(R.drawable.ic_android)
            .build()
    }

    private suspend fun outputLog(delayMs: Long) = withContext(Dispatchers.Default) {
        delay(delayMs)
        Log.d(TAG, "LoggingService done(delay $delayMs msec)")

        stopSelf()
    }
}