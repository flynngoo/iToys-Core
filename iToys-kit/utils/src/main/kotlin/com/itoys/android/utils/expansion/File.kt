package com.itoys.android.utils.expansion

import okio.buffer
import okio.sink
import okio.source
import java.io.File

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/24
 */

fun String.toFile() = File(this)

/**
 * 获取文件md5
 */
fun File.md5() = source().buffer().buffer.md5().hex()