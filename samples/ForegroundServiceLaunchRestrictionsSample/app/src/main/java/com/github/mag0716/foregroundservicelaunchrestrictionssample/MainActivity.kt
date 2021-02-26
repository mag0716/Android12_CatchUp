package com.github.mag0716.foregroundservicelaunchrestrictionssample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ForegroundServiceRestriction"
    }

    private lateinit var startForegroundServiceButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startForegroundServiceButton = findViewById(R.id.startForegroundServiceButton)
        startForegroundServiceButton.setOnClickListener {
            startForegroundService()
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
}