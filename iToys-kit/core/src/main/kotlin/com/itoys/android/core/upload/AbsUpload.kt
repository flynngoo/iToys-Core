package com.itoys.android.core.upload

import com.itoys.android.utils.HOUR
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.isNotBlank

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/18
 */
abstract class AbsUpload : IUploadRepository {

    /**
     * 保存公开token
     */
    open fun savePublicToken(token: String?) {
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
    open fun savePrivateToken(token: String?) {
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
}