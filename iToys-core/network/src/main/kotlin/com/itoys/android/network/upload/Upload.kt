package com.itoys.android.network.upload

import com.itoys.android.utils.expansion.toFile
import com.itoys.android.utils.mmkv.MMKVOwner
import com.itoys.android.utils.mmkv.mmkvLong
import com.itoys.android.utils.mmkv.mmkvString
import com.tencent.mmkv.MMKV
import java.io.File

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/1/1
 */
object Upload : MMKVOwner {

    private const val MMKV_FILE_ID = "iToys-Upload"

    override val mmkv: MMKV
        get() = MMKV.mmkvWithID(MMKV_FILE_ID)

    /**
     * 公开文件token
     */
    var publicToken: String? by mmkvString()

    /**
     * 公开文件token 时间
     */
    var publicTokenTime: Long by mmkvLong()

    /**
     * 私有文件token
     */
    var privateToken: String? by mmkvString()

    /**
     * 公开文件token 时间
     */
    var privateTokenTime: Long by mmkvLong()
}

/**
 * string list -> file list.
 */
fun Iterable<String>.toFileList() = map(String::toFile)