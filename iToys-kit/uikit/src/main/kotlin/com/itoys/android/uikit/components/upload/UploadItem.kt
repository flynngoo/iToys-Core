package com.itoys.android.uikit.components.upload

import com.itoys.android.uikit.components.upload.exception.EmptyImageException
import com.itoys.android.uikit.components.upload.exception.UploadFailedException
import com.itoys.android.utils.SysUtils
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.isNotBlank
import java.io.File
import java.util.UUID

/**
 * @Author Flynn Gu
 * @Email gufanfan.worker@gmail.com
 * @Date 2025/11/20
 */
data class UploadItem(
    val id: String = UUID.randomUUID().toString(),
    val localPath: String,
    val type: UploadType,
    val isCanDelete: Boolean,
) {
    var state: UploadState = UploadState.PENDING

    var url: String = ""

    var file: File? = null
}

fun UploadItem?.isEmpty() = this?.url.isBlank() && this?.localPath.isBlank()

fun UploadItem.itemUrl() = localPath.invalid(url)

fun String.toImageItem(isCanDelete: Boolean = true) = UploadItem(
    localPath = this,
    type = UploadType.IMAGE,
    isCanDelete = isCanDelete,
)

fun String.toImageUrlItem(isCanDelete: Boolean = true): UploadItem {
    val item = UploadItem(
        localPath = this,
        type = UploadType.IMAGE,
        isCanDelete = isCanDelete,
    )

    item.url = this
    item.state = UploadState.Success(item.id, this)

    return item
}

fun Iterable<String>.toImageItemList(): List<UploadItem> = map { it.toImageItem() }

fun Iterable<String>.toImageUrlItemList(isCanDelete: Boolean = true): List<UploadItem> {
    return map {
        val item = it.toImageItem(isCanDelete)
        item.url = it
        item.state = UploadState.Success(item.id, it)
        item
    }
}

fun Iterable<UploadItem>.toImageUrlList(): List<String> = map { it.itemUrl() }

fun String.toFileItem(isCanDelete: Boolean = true) = UploadItem(
    localPath = this,
    type = UploadType.DOCUMENT,
    isCanDelete = isCanDelete,
)

fun String.toFileUrlItem(isCanDelete: Boolean = true): UploadItem {
    val item = UploadItem(
        localPath = this,
        type = UploadType.DOCUMENT,
        isCanDelete = isCanDelete,
    )

    item.url = this
    item.state = UploadState.Success(item.id, this)

    return item
}

fun Iterable<String>.toFileItemList(isCanDelete: Boolean = true): List<UploadItem> {
    return map {
        val item = it.toFileItem(isCanDelete)
        item.url = it
        item.state = UploadState.Success(item.id, it)
        item
    }
}

fun UploadItem.verifyUrl(label: String, required: Boolean = true): String {
    val isProvided = url.invalid(localPath).isNotBlank()
    if (required && !isProvided) {
        throw EmptyImageException("请上传$label")
    }
    if (isProvided && !SysUtils.isHttpUrl(url)) throw UploadFailedException("${label}上传失败, 请重试")

    return url
}