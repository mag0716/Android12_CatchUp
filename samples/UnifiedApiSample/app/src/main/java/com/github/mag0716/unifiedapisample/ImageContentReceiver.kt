package com.github.mag0716.unifiedapisample

import android.util.Log
import android.view.View
import androidx.core.view.ContentInfoCompat
import androidx.core.view.OnReceiveContentListener

class ImageContentReceiver : OnReceiveContentListener {

    companion object {
        private const val TAG = "ImageContentReceiver"
        val MIME_TYPES = arrayOf("image/*")
    }

    override fun onReceiveContent(view: View, payload: ContentInfoCompat): ContentInfoCompat? {
        val split = payload.partition {
            it.uri != null
        }
        val uriContent = split.first
        val remaining = split.second

        if (uriContent != null) {
            val clipData = uriContent.clip
            for (i in 0 until clipData.itemCount) {
                val uri = clipData.getItemAt(i).uri
                Log.d(TAG, "onReceiveContent : $uri")
            }
        }

        return remaining
    }
}