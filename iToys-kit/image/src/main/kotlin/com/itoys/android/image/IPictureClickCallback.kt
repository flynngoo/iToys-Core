package com.itoys.android.image

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/20
 */
interface IPictureClickCallback {

    /**
     * 选择图片
     */
    fun selectPicture()

    /**
     * 删除图片
     */
    fun delete(index: Int)
}