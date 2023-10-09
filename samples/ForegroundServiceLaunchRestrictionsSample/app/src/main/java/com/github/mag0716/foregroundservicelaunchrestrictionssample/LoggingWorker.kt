package com.github.mag0716.foregroundservicelaunchrestrictionssample

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.*
import kotlinx.coroutines.delay

class LoggingWorker(
    context: Context,
    parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {

    companion object {
        private const val KEY_DELAY = "Delay"

        @SuppressLint("UnsafeExperimentalUsageError")
        fun createWorkRequest(delayMs: Long): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<LoggingWorker>()
                .setInputData(
                    workDataOf(
                        KEY_DELAY to delayMs
                    )
                )
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
        }
    }

    override suspend fun doWork(): Result {
        val delayMs = inputData.getLong(KEY_DELAY, 0L)
        delay(delayMs)
        Log.d(MainActivity.TAG, "LoggingWorker done(delay $delayMs msec)")

        return Result.success()
    }
}