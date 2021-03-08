package com.github.mag0716.unifiedapisample

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
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
                if (view is EditText) {
                    insertImage(view, uri)
                }
            }
        }

        return remaining
    }

    private fun insertImage(editText: EditText, uri: Uri) {
        val context = editText.context
        val bitmap = MediaStore.Images.Media.getBitmap(
            context.contentResolver,
            uri
        )
        val drawable = BitmapDrawable(context.resources, bitmap)
        Log.d(TAG, "insertImage : $uri, $drawable")
        editText.setCompoundDrawablesWithIntrinsicBounds(
            null,
            drawable,
            null,
            null
        )
    }
}