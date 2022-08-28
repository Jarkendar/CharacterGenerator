package com.skrzypczak.charactergenerator

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream


class CardSaver(private val context: Context, private val ioScope: CoroutineScope) {

    @Throws(IOException::class)
    fun saveBitmap(bitmap: Bitmap?, format: Bitmap.CompressFormat,
        mimeType: String, displayName: String
    ): Deferred<Uri?> {
        return ioScope.async {
            if (bitmap == null) {
                return@async null
            }

            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            }

            val resolver = context.contentResolver
            var uri: Uri? = null

            try {
                uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    ?: throw IOException("Failed to create new MediaStore record.")

                resolver.openOutputStream(uri)?.use {
                    if (!bitmap.compress(format, 95, it))
                        throw IOException("Failed to save bitmap.")
                } ?: throw IOException("Failed to open output stream.")

                return@async uri
            } catch (e: IOException) {

                uri?.let { orphanUri ->
                    // Don't leave an orphan entry in the MediaStore
                    resolver.delete(orphanUri, null, null)
                }

                return@async null
            }
        }
    }

    fun getDrawableFromUri(uri: Uri?): Drawable? {
        try {
            if (uri == null) {
                return null
            }
            val inputStream: InputStream = context.contentResolver.openInputStream(uri) ?: return null
            return Drawable.createFromStream(inputStream, uri.toString())
        } catch (e: FileNotFoundException) {
            return null //todo default placeholder
        }
    }
}