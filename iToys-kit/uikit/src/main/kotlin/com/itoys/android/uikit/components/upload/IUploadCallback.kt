package com.itoys.android.uikit.components.upload

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/9
 */
interface IUploadCallback {

    /**
     * 点击上传
     */
    fun upload(mark: String)

    /**
     * 删除
     */
    fun delete(mark: String)
}