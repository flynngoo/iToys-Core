package com.itoys.android.uikit.components.upload

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/9
 */
interface IUploadCallback {

    /**
     * 自定义图片选择
     */
    fun customImageSelection() {}

    /**
     * 删除
     */
    fun delete(mark: String) {}

    /**
     * 点击上传
     *
     * @param mark 标记
     * @param path 路径
     */
    fun upload(mark: String, path: String)
}