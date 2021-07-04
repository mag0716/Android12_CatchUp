package com.github.mag0716.splashscreensample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
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

        viewModel.hasError.observe(this) { hasError ->
            if (hasError) {
                showRetryError()
            }
        }

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
        return if (viewModel.isReady.value == true) {
            content.viewTreeObserver.removeOnPreDrawListener(this)
            true
        } else {
            false
        }
    }

    // スプラッシュ画面表示中はバックキーのイベントが飛んでこない
    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed")
        super.onBackPressed()
    }

    private fun showRetryError() {
        // スプラッシュ画面表示中はダイアログは表示できない
//        AlertDialog.Builder(this)
//            .setTitle("Error")
//            .setMessage("Please Retry")
//            .setPositiveButton("retry") { _, _ ->
//                viewModel.retry()
//            }
//            .create()
//            .show()

        Toast.makeText(this, "retry", Toast.LENGTH_LONG).show()
    }

    companion object {
        const val TAG = "SplashScreenSample"
    }
}