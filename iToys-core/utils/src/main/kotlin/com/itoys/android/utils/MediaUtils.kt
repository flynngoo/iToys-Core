package com.itoys.android.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import java.io.File
import java.io.FileOutputStream

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 01/06/2021 19:36
 * @desc 媒体相关工具类
 */
object MediaUtils {

    fun saveMediaImage(context: Context, dir: String, bitmap: Bitmap): String {
        val displayName = "export_${System.currentTimeMillis()}"
        return if (SysUtils.isAndroid10()) {
            saveMediaImageAndroidQ(context, dir, displayName, bitmap)
        } else {
            saveMediaImageOther(context, dir, displayName, bitmap)
        }
    }

    private fun saveMediaImageAndroidQ(context: Context, dir: String, displayName: String, bitmap: Bitmap): String {
        val path = "${PathUtils.picturesPath()}${dir}"

        val resolver = context.contentResolver
        val imageCollection =
            MediaStore.Images.Media.getContentUri(
                if (SysUtils.isAndroid10()) {
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                } else {
                    MediaStore.VOLUME_EXTERNAL
                }
            )
        val newImageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (SysUtils.isAndroid10()) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
                put(MediaStore.Images.Media.RELATIVE_PATH, path)
            }
        }
        val imageUri = resolver.insert(imageCollection, newImageDetails)
        imageUri?.let { uri ->
            resolver.openOutputStream(uri, "w").use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it!!)
            }

            newImageDetails.clear()
            if (SysUtils.isAndroid10()) {
                newImageDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
            }
            resolver.update(uri, newImageDetails, null, null)
        }

        return "sdcard/$path/${displayName}.jpg"
    }

    private fun saveMediaImageOther(context: Context, dir: String, displayName: String, bitmap: Bitmap): String {
        val path = "${PathUtils.picturesPath()}${dir}"
        val dirFile = File(path)
        FileUtils.createOrExistsDir(dirFile)
        val imageFile = File(dirFile, "${displayName}.jpg")
        imageFile.createNewFile()
        val ops = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ops)
        ops.flush()
        IOUtils.closeIO(ops)
        context.sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(imageFile)
            )
        )
        return "sdcard/$path/${displayName}.jpg"
    }

    /**
     * view转bitmap
     *
     * @param view view对象
     * @return drawable
     */
    fun view2Bitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }
}