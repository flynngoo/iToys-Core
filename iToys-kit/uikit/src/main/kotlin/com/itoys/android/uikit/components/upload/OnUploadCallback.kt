package com.itoys.android.uikit.components.upload

import java.io.File

/**
 * @Author Flynn Gu
 * @Email gufanfan.worker@gmail.com
 * @Date 2025/11/21
 */
interface OnUploadCallback {

    fun onFileSelected(mark: String, item: UploadItem) {}

    fun onFileSelected(mark: String, items: List<UploadItem>) {}

    fun onSelectedFailed(mark: String, message: String) {}

    fun onFileDeselected(mark: String, position: Int) {}
}