package com.itoys.android.core.network.upload

import com.itoys.android.core.network.ResultException
import java.io.File

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/1/1
 */
interface IUploadRepository {

    /**
     * 获取token
     */
    suspend fun getToken(tokenType: TokenType): String?

    /**
     * 公开token
     */
    suspend fun getPublicToken(): String?

    /**
     * 私有token
     */
    suspend fun getPrivateToken(): String?

    /**
     * 上传文件
     */
    suspend fun uploadFile(
        filePath: String,
        category: Category,
        tokenType: TokenType,
        folder: String,
        success: ((String) -> Unit)?,
        handleEx: ((ResultException) -> Unit)?
    )

    /**
     * 上传文件
     */
    suspend fun uploadFile(
        file: File,
        category: Category,
        tokenType: TokenType,
        folder: String,
        success: ((String) -> Unit)?,
        handleEx: ((ResultException) -> Unit)?
    )

    /**
     * 同步上传文件
     */
    suspend fun syncUploadFile(
        file: File,
        uploadKey: String,
        tokenType: TokenType,
    ): String

    /**
     * 上传文件 list
     */
    suspend fun uploadFileList(
        files: List<File>,
        category: Category,
        tokenType: TokenType,
        folder: String,
        success: ((List<String>) -> Unit)?,
        handleEx: ((Int) -> Unit)?
    )

    /**
     * 生成上传key
     */
    fun generateUploadKey(file: File, folder: String, category: Category): String
}