package com.cleverlycode.getwheels.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

object InternalStorageUtils {
    fun saveImageToInternalStorage(
        context: Context,
        dirName: String,
        fileName: String,
        bitmap: Bitmap
    ): Boolean {
        return try {
            val fileDirectory = context.getDir(dirName, Context.MODE_PRIVATE)
            val file = File(fileDirectory, fileName)
            if (file.exists()) {
                file.delete()
            }

            file.createNewFile()
            file.outputStream().use { fileOutputStream ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                    throw IOException("Could not save the image")
                }
            }
            true
        } catch (exception: IOException) {
            exception.printStackTrace()
            false
        }
    }

    fun getImageFromInternalStorage(context: Context, dirName: String, fileName: String): Bitmap? {
        val fileDirectory = context.getDir(dirName, Context.MODE_PRIVATE)
        return try {
            val fileInputStream = File(fileDirectory, fileName).inputStream()
            BitmapFactory.decodeStream(fileInputStream)
        } catch (exception: Exception) {
            if (exception !is FileNotFoundException) {
                File(fileDirectory, fileName).delete()
            }
            null
        }
    }

    fun deleteImageFromInternalStorage(context: Context, dirName: String, fileName: String) {
        val fileDirectory = context.getDir(dirName, Context.MODE_PRIVATE)
        try {
            val file = File(fileDirectory, fileName)
            file.delete()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun deleteAllImagesFromInternalStorage(context: Context, dirName: String) {
        val fileDirectory = context.getDir(dirName, Context.MODE_PRIVATE)
        fileDirectory.delete()
    }
}