package com.itoys.android.simple.upload

import android.util.Log
import com.itoys.android.core.network.ApiResultCode
import com.itoys.android.core.network.NetworkInitialization
import com.itoys.android.core.network.ResultException
import com.itoys.android.core.network.argsToBody
import com.itoys.android.core.network.result
import com.itoys.android.core.upload.AbsUpload
import com.itoys.android.core.upload.TokenType
import com.itoys.android.logcat.asLog
import com.itoys.android.logcat.logcat
import com.itoys.android.utils.TimeUtils
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.then
import com.qiniu.android.storage.UpCancellationSignal
import com.qiniu.android.storage.UpProgressHandler
import com.qiniu.android.storage.UploadManager
import com.qiniu.android.storage.UploadOptions
import java.io.File

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/18
 */
class JJQNUpload(private val api: JJUploadApi) : AbsUpload() {

    /** 七牛 upload manager */
    private val uploadManager by lazy { UploadManager() }

    override fun generateUploadKey(file: File, folder: String, category: String): String {
        var uploadPath = ""
        // 1.1 当前时间
        uploadPath += TimeUtils.nowTimeString()
        // 1.2 文件类型
        uploadPath += checkFolder(category)
        // 1.3 目录文件夹
        uploadPath += checkFolder(folder)

        // 1.3 文件名
        uploadPath += "/${System.currentTimeMillis()}-${file.name}"

        return uploadPath
    }

    override suspend fun getToken(tokenType: TokenType): String? {
        var token = (tokenType == TokenType.Public).then(getPublicToken(), getPrivateToken())

        if (token.isBlank()) {
            try {
                val result = api.qnToken(argsToBody("type" to tokenType.type)).result()
                token = result?.token

                if (tokenType == TokenType.Public) {
                    savePublicToken(token)
                } else {
                    savePrivateToken(token)
                }
            } catch (e: Exception) {
                logcat(priority = Log.ERROR) { e.asLog() }
            }
        }

        return token
    }

    override suspend fun syncUploadFile(
        file: File,
        uploadKey: String,
        tokenType: TokenType
    ): String {
        // 1: 获取token
        val token = getToken(tokenType)

        if (token.isBlank()) {
            return ""
        }

        // 2.1 上传进度
        val processHandler = UpProgressHandler { _, percent ->
            val process = (percent * 100).toInt()
            logcat { "Upload progress ${process}%" }
        }

        // 2.2 取消上传
        val cancellationSignal = UpCancellationSignal { false }

        // 2.3 上传
        val info = uploadManager.syncPut(
            file,
            uploadKey,
            token,
            UploadOptions(null, null, false, processHandler, cancellationSignal)
        )

        return if (info.isOK) {
            logcat { "上传成功 -> $info" }
            (tokenType == TokenType.Public).then(
                "${NetworkInitialization.requireResourcesHostUrl()}$uploadKey",
                uploadKey
            )
        } else {
            logcat(priority = Log.ERROR) { "$uploadKey 上传失败 -> $info" }
            ""
        }
    }

    suspend fun syncUploadFileList(
        filePath: List<String>,
        category: String,
        folder: String,
        tokenType: TokenType
    ): List<String> {
        val uploadedKeyList = arrayListOf<String>()

        filePath.toList().forEachIndexed { index, path ->
            logcat { "No.${index} 开始上传." }

            val uploadedKey = if (path.startsWith("http")) {
                path
            } else {
                val file = File(path)
                syncUploadFile(file, generateUploadKey(file, folder, category), tokenType)
            }

            if (uploadedKey.isNotBlank()) {
                logcat { "No.${index} 文件上传成功, upload path -> $uploadedKey" }
                uploadedKeyList.add(uploadedKey)
            } else {
                logcat { "No.${index} 文件上传失败!" }
            }

            logcat { "No.${index} 上传结束." }
        }

        return uploadedKeyList
    }

    suspend fun syncUploadFileList(
        filePath: List<String>,
        category: String,
        folder: String,
        tokenType: TokenType,
        progress: ((Int) -> Unit)?,
    ): List<String> {
        val uploadedKeyList = arrayListOf<String>()

        filePath.toList().forEachIndexed { index, path ->
            logcat { "No.${index} 开始上传." }

            val uploadedKey = if (path.startsWith("http")) {
                path
            } else {
                val file = File(path)
                syncUploadFile(file, generateUploadKey(file, folder, category), tokenType)
            }

            if (uploadedKey.isNotBlank()) {
                logcat { "No.${index} 文件上传成功, upload path -> $uploadedKey" }
                uploadedKeyList.add(uploadedKey)
                progress?.invoke(index + 1)
            } else {
                logcat { "No.${index} 文件上传失败!" }
            }

            logcat { "No.${index} 上传结束." }
        }

        return uploadedKeyList
    }

    /**
     * 异步上传图片
     */
    suspend fun asyncUploadFileList(
        filePath: List<String>,
        category: String,
        folder: String,
        tokenType: TokenType,
        progressCallback: ((Int) -> Unit)?,
        success: ((List<String>) -> Unit)?,
    ) {
        val uploadedKeyList = arrayListOf<String>()

        filePath.toList().forEachIndexed { index, path ->
            logcat { "No.${index} 开始上传." }

            val uploadedKey = if (path.startsWith("http")) {
                path
            } else {
                val file = File(path)
                syncUploadFile(file, generateUploadKey(file, folder, category), tokenType)
            }

            if (uploadedKey.isNotBlank()) {
                logcat { "No.${index} 文件上传成功, upload path -> $uploadedKey" }
                uploadedKeyList.add(uploadedKey)
                progressCallback?.invoke(index + 1)
            } else {
                logcat { "No.${index} 文件上传失败!" }
            }

            logcat { "No.${index} 上传结束." }
        }

        success?.invoke(uploadedKeyList)
    }

    override suspend fun uploadFile(
        file: File,
        category: String,
        tokenType: TokenType,
        folder: String,
        progress: ((Int) -> Unit)?,
        success: ((String) -> Unit)?,
        handleEx: ((ResultException) -> Unit)?
    ) {
        // 1: 获取token
        val token = getToken(tokenType)

        if (token.isBlank()) {
            handleEx?.invoke(ResultException(ApiResultCode.TOKEN_ERROR, "上传失败, 请检查网络"))
            return
        }

        // 2. 组装上传文件地址
        val uploadKey = generateUploadKey(file, folder, category)

        // 3.1 上传进度
        val processHandler = UpProgressHandler { _, percent ->
            val process = (percent * 100).toInt()
            logcat { "Upload progress ${process}%" }
            progress?.invoke(process)
        }

        // 3.2 取消上传
        val cancellationSignal = UpCancellationSignal { false }

        // 3.3 上传
        uploadManager.put(
            file, uploadKey, token,
            { key, info, _ ->
                if (info.isOK) {
                    logcat { "上传成功 -> $info" }

                    success?.invoke(
                        (tokenType == TokenType.Public).then(
                            "${NetworkInitialization.requireResourcesHostUrl()}$key",
                            key
                        )
                    )
                } else {
                    logcat(priority = Log.ERROR) { "上传失败 -> $info" }
                    handleEx?.invoke(ResultException(ApiResultCode.PARAMS_ERROR, "上传失败, 请重试"))
                }
            }, UploadOptions(null, null, false, processHandler, cancellationSignal)
        )
    }

    override suspend fun uploadFile(
        filePath: String,
        category: String,
        tokenType: TokenType,
        folder: String,
        progress: ((Int) -> Unit)?,
        success: ((String) -> Unit)?,
        handleEx: ((ResultException) -> Unit)?
    ) {
        if (filePath.startsWith("http")) {
            success?.invoke(filePath)
        } else {
            uploadFile(File(filePath), category, tokenType, folder, progress, success, handleEx)
        }
    }

    override suspend fun uploadFileList(
        files: List<File>,
        category: String,
        tokenType: TokenType,
        folder: String,
        success: ((List<String>) -> Unit)?,
        handleEx: ((Int) -> Unit)?
    ) {
        val uploadedKeyList = arrayListOf<String>()

        files.toList().forEachIndexed { index, file ->
            logcat { "No.${index} 开始上传." }

            val uploadedKey = syncUploadFile(file, generateUploadKey(file, folder, category), tokenType)
            if (uploadedKey.isNotBlank()) {
                logcat { "No.${index} 文件上传成功, upload path -> $uploadedKey" }
                uploadedKeyList.add(uploadedKey)
            } else {
                logcat { "No.${index} 文件上传失败!" }
                handleEx?.invoke(index)
            }

            logcat { "No.${index} 上传结束." }
        }

        success?.invoke(uploadedKeyList)
    }

    /**
     * 检查 folder path.
     */
    private fun checkFolder(folder: String): String {
        if (folder.isNotBlank() && !folder.startsWith("/")) {
            return File.separatorChar.plus(folder)
        }

        return folder
    }
}