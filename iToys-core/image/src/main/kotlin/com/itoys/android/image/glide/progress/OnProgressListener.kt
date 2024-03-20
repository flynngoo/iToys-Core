package com.itoys.android.image.glide.progress

// 给函数起别名 便于调用和书写
typealias OnProgressListener = ((isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long) -> Unit)?