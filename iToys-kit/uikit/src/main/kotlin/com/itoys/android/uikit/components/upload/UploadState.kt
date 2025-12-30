package com.itoys.android.uikit.components.upload

/**
 * @Author Flynn Gu
 * @Email gufanfan.worker@gmail.com
 * @Date 2025/11/20
 */
sealed class UploadState {
    data object PENDING : UploadState()

    data class Progress(val uploadId: String, val progress: Int) : UploadState()

    data class Success(val uploadId: String, val url: String) : UploadState()

    data class Error(val uploadId: String, val message: String) : UploadState()
}