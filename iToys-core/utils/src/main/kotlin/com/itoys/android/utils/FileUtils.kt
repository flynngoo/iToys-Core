package com.itoys.android.utils

import java.io.File

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/14
 */
object FileUtils {

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return boolean
     */
    fun createOrExistsDir(file: File?) = (file != null) && if (file.exists()) file.isDirectory else file.mkdirs()
}