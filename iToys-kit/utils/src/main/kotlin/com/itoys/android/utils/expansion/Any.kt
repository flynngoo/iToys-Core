package com.itoys.android.utils.expansion

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/5/26
 */
fun Any?.isNull(): Boolean {
    return if (this is String) {
        this.isBlank()
    } else {
        this == null
    }
}

fun Any?.isNotNull(): Boolean {
    return !isNull()
}