package com.itoys.android.uikit.components.image

import android.app.Activity

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/20
 */
interface IViewImageCallback {

    fun onDownloadStart(activity: Activity?, position: Int) {}

    fun onDownloadSuccess(activity: Activity?, position: Int, target: String) {}

    fun onDownloadFailed(activity: Activity?, position: Int) {}

    fun onDelete(position: Int) {}
}