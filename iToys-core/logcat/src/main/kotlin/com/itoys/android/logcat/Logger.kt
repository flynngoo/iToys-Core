package com.itoys.android.logcat

import android.util.Log
import timber.log.Timber

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/20
 */

fun Any.logcat(
    priority: Int = Log.DEBUG,
) {
    val message = this.toString()

    when (priority) {
        Log.VERBOSE -> Timber.v(message)
        Log.DEBUG -> Timber.d(message)
        Log.INFO -> Timber.i(message)
        Log.WARN -> Timber.w(message)
        Log.ERROR -> Timber.e(message)
        Log.ASSERT -> Timber.wtf(message)
        else -> Timber.log(priority, message)
    }
}

fun Throwable.logcat() {
    Timber.e(this.asLog())
}

inline fun logcat(
    priority: Int = Log.DEBUG,
    message: () -> String
) {
    when (priority) {
        Log.VERBOSE -> Timber.v(message())
        Log.DEBUG -> Timber.d(message())
        Log.INFO -> Timber.i(message())
        Log.WARN -> Timber.w(message())
        Log.ERROR -> Timber.e(message())
        Log.ASSERT -> Timber.wtf(message())
        else -> Timber.log(priority, message())
    }
}

