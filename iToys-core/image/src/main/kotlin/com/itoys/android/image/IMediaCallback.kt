package com.itoys.android.image

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/20
 */
abstract class IMediaCallback {

    open fun onResult(result: ImageMedia) {}

    open fun onResult(result: List<ImageMedia>?) {}

    open fun onCancel() {}
}