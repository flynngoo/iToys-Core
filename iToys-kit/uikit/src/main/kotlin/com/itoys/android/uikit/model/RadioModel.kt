package com.itoys.android.uikit.model

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/1/1
 */
data class RadioModel(
    val id: Int,
    val type: Int,
    val label: String
) {
    var isChecked = false
}

fun RadioModel.copyOf(): RadioModel {
    val copy = copy()
    copy.isChecked = isChecked
    return copy
}