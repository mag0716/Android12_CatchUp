package com.github.mag0716.unifiedapisample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.view.ContentInfoCompat
import androidx.core.view.OnReceiveContentListener
import kotlinx.coroutines.*

class ImageContentReceiver : OnReceiveContentListener {

    companion object {
        private const val TAG = "ImageContentReceiver"
        val MIME_TYPES = arrayOf("image/*")
    }

    private val scope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.Main +
                CoroutineExceptionHandler { _, throwable ->
                    Log.w(
                        "ImageContentReceiver",
                        throwable
                    )
                })

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
        scope.launch {
            val context = editText.context
            val bitmap = loadImage(context, uri)
            val drawable = BitmapDrawable(context.resources, bitmap)
            editText.setCompoundDrawablesWithIntrinsicBounds(
                null,
                drawable,
                null,
                null
            )
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext") // 実際にはバックグラウンドスレッドで実行されるのがwarningが出てしあうので抑制する
    private suspend fun loadImage(context: Context, uri: Uri): Bitmap =
        withContext(Dispatchers.IO) {
            val contentResolver = context.contentResolver
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(
                    contentResolver,
                    uri
                )
            } else {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        contentResolver,
                        uri
                    )
                )
            }
        }
}