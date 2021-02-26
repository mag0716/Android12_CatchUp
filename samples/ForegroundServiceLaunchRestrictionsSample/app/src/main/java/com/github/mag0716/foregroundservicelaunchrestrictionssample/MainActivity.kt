package com.github.mag0716.foregroundservicelaunchrestrictionssample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.work.WorkManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * FIXME：https://issuetracker.google.com/issues/180884673#comment2 により WorkManager 2.7.0-alpha01 に依存するとアプリが起動できなくなる
 */
class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ForegroundServiceRestriction"
    }

    private lateinit var startForegroundServiceButton: Button
    private lateinit var startWorkerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startForegroundServiceButton = findViewById(R.id.startForegroundServiceButton)
        startForegroundServiceButton.setOnClickListener {
            startForegroundService()
        }
        startWorkerButton = findViewById(R.id.startWorkerButton)
        startWorkerButton.setOnClickListener {
            startWorker()
        }
    }

    private fun startForegroundService() {
        // 実装を簡略化するためにGlobalScopeを利用
        GlobalScope.launch {
            delay(5000)
            try {
                val intent = LoggingService.createService(applicationContext, 15000)
                startForegroundService(intent)
            } catch (exception: Exception) {
                Log.w(TAG, "failed startForegroundService", exception)
            }
        }
    }

    private fun startWorker() {
        val workRequest = LoggingWorker.createWorkRequest(5000)
        val workManager = WorkManager.getInstance(this)
        workManager.cancelAllWork()
        workManager.beginWith(workRequest)
            .enqueue()
    }
}