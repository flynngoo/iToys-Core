package com.itoys.android.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/14
 */
object FileUtils {

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return boolean
     */
    fun createOrExistsDir(file: File?) = (file != null) && if (file.exists()) file.isDirectory else file.mkdirs()

    /**
     * 把 Uri 复制成 cache 中的临时 File
     */
    fun uriToFile(context: Context, uri: Uri): File? {
        val contentResolver = context.contentResolver

        // 获取文件名
        val fileName = getFileName(contentResolver, uri) ?: UUID.randomUUID().toString()

        // 目标文件：放到 cacheDir，上传完你可选择删除
        val tempFile = File(context.cacheDir, fileName)

        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return tempFile
    }

    /**
     * 从 Uri 获取文件名
     */
    fun getFileName(resolver: ContentResolver, uri: Uri): String? {
        var name: String? = null

        resolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex >= 0) {
                name = cursor.getString(nameIndex)
            }
        }

        if (name == null) {
            // fallback，从路径获取
            name = uri.path?.substringAfterLast('/')
        }

        return name
    }
}