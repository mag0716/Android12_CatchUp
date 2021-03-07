package com.github.mag0716.unifiedapisample

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat

class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.edit_text)
        ViewCompat.setOnReceiveContentListener(
            editText,
            ImageContentReceiver.MIME_TYPES,
            ImageContentReceiver()
        )
    }
}