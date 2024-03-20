package com.itoys.android.housing

import com.itoys.android.utils.mmkv.MMKVOwner
import com.itoys.android.utils.mmkv.mmkvBoolean
import com.tencent.mmkv.MMKV

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/3/4
 */
object IToys : MMKVOwner {

    private const val MMKV_FILE_ID = "iToys"

    override val mmkv: MMKV
        get() = MMKV.mmkvWithID(MMKV_FILE_ID)

    /**
     * 统一隐私政策
     */
    var AgreePrivacy: Boolean by mmkvBoolean(default = true)
}