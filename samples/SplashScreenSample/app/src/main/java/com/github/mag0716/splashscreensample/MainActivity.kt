package com.github.mag0716.splashscreensample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), ViewTreeObserver.OnPreDrawListener {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var content: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_main)
        content = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(this)

        if (savedInstanceState == null) {
            viewModel.init()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        content.viewTreeObserver.removeOnPreDrawListener(this)
    }

    override fun onPreDraw(): Boolean {
        if (viewModel.isReady.value == true) {
            content.viewTreeObserver.removeOnPreDrawListener(this)
            return true
        } else {
            return false
        }
    }

    companion object {
        const val TAG = "SplashScreenSample"
    }
}