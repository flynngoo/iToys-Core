package com.itoys.android.network.upload.qiniu

import android.util.Log
import com.itoys.android.logcat.asLog
import com.itoys.android.logcat.logcat
import com.itoys.android.network.ApiResultCode
import com.itoys.android.network.BuildConfig
import com.itoys.android.network.ResultException
import com.itoys.android.network.argsToBody
import com.itoys.android.network.result
import com.itoys.android.network.upload.Category
import com.itoys.android.network.upload.IUploadRepository
import com.itoys.android.network.upload.TokenType
import com.itoys.android.network.upload.Upload
import com.itoys.android.utils.HOUR
import com.itoys.android.utils.TimeUtils
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.isNotBlank
import com.itoys.android.utils.expansion.then
import com.qiniu.android.storage.UpCancellationSignal
import com.qiniu.android.storage.UpProgressHandler
import com.qiniu.android.storage.UploadManager
import com.qiniu.android.storage.UploadOptions
import java.io.File

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/1/1
 */
class QiNiuRepository(
    private val api: QiNiuApi
) : IUploadRepository {

    /** 七牛 upload manager */
    private val uploadManager by lazy { UploadManager() }

    override suspend fun getToken(tokenType: TokenType): String? {
        var token = (tokenType == TokenType.Public).then(getPublicToken(), getPrivateToken())

        if (token.isBlank()) {
            try {
                token = api.token(argsToBody("type" to tokenType.type)).result()
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

    /**
     * 保存公开token
     */
    private fun savePublicToken(token: String?) {
        if (token.isBlank()) return

        Upload.publicToken = token
        Upload.publicTokenTime = System.currentTimeMillis()
    }

    override suspend fun getPublicToken(): String? {
        val token = Upload.publicToken
        val nowTime = System.currentTimeMillis()
        return if (nowTime - Upload.publicTokenTime < HOUR && token.isNotBlank()) {
            token
        } else {
            ""
        }
    }

    /**
     * 保存私有token
     */
    private fun savePrivateToken(token: String?) {
        if (token.isBlank()) return

        Upload.privateToken = token
        Upload.privateTokenTime = System.currentTimeMillis()
    }

    override suspend fun getPrivateToken(): String? {
        val token = Upload.privateToken
        val nowTime = System.currentTimeMillis()
        return if (nowTime - Upload.privateTokenTime < HOUR && token.isNotBlank()) {
            token
        } else {
            ""
        }
    }

    override suspend fun uploadFile(
        filePath: String,
        category: Category,
        tokenType: TokenType,
        folder: String,
        success: ((String) -> Unit)?,
        handleEx: ((ResultException) -> Unit)?
    ) {
        uploadFile(File(filePath), category, tokenType, folder, success, handleEx)
    }

    override suspend fun uploadFile(
        file: File,
        category: Category,
        tokenType: TokenType,
        folder: String,
        success: ((String) -> Unit)?,
        handleEx: ((ResultException) -> Unit)?
    ) {
        // 1: 获取token
        val token = getToken(tokenType)

        if (token.isBlank()) {
            handleEx?.invoke(ResultException(ApiResultCode.TOKEN_ERROR, "token不能为空"))
            return
        }

        // 2. 组装上传文件地址
        val uploadKey = generateUploadKey(file, folder, category)

        // 3.1 上传进度
        val processHandler = UpProgressHandler { _, percent ->
            val process = (percent * 100).toInt()
            logcat { "Upload progress ${process}%" }
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
                            "${BuildConfig.PUBLIC_RES_DOMAIN}$key", key
                        )
                    )
                } else {
                    logcat(priority = Log.ERROR) { "上传失败 -> $info" }
                    handleEx?.invoke(ResultException(ApiResultCode.ARGS_ERROR, "File上传失败"))
                }
            }, UploadOptions(null, null, false, processHandler, cancellationSignal)
        )
    }

    override suspend fun syncUploadFile(
        file: File,
        uploadKey: String,
        tokenType: TokenType,
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

        if (info.isOK) {
            logcat { "上传成功 -> $info" }
            return (tokenType == TokenType.Public).then("${BuildConfig.PUBLIC_RES_DOMAIN}$uploadKey", uploadKey)
        } else {
            logcat(priority = Log.ERROR) { "$uploadKey 上传失败 -> $info" }
            return ""
        }
    }

    override suspend fun uploadFileList(
        files: List<File>,
        category: Category,
        tokenType: TokenType,
        folder: String,
        success: ((List<String>) -> Unit)?,
        handleEx: ((Int) -> Unit)?
    ) {
        val uploadedKeyList = arrayListOf<String>()

        files.mapIndexed { index, file ->
            logcat { "No.${index} 开始上传." }

            val uploadedKey = syncUploadFile(file, generateUploadKey(file, folder, category), tokenType)
            if (uploadedKey.isNotBlank()) {
                logcat { "No.${index} 文件上传成功, upload path -> $uploadedKey" }
                uploadedKeyList.add(uploadedKey.invalid())
            } else {
                logcat { "No.${index} 文件上传失败!" }
                handleEx?.invoke(index)
            }

            logcat { "No.${index} 上传结束." }
        }

        success?.invoke(uploadedKeyList)
    }

    override fun generateUploadKey(file: File, folder: String, category: Category): String {
        var uploadPath = ""
        // 1.1 文件类型
        uploadPath += checkFolder(category.category)
        // 1.2 目录文件夹
        uploadPath += checkFolder(folder)
        // 1.3 当前时间
        uploadPath += TimeUtils.nowTimeString()
        // 1.3 文件名
        uploadPath += "/${file.name}"

        return uploadPath
    }

    /**
     * 检查 folder path.
     */
    private fun checkFolder(folder: String): String {
        if (folder.isNotBlank() && !folder.endsWith("/")) {
            return folder.plus(File.separatorChar)
        }

        return folder
    }
}