package com.github.mag0716.compose.foregroundservicelaunchrestrictionssample

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var startForegroundServiceButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startForegroundServiceButton = findViewById(R.id.startForegroundServiceButton)
        startForegroundServiceButton.setOnClickListener {
            val intent = LoggingService.createService(this, 15000)
            startForegroundService(intent)
        }
    }
}