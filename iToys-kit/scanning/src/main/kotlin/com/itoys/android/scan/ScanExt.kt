package com.itoys.android.scan

import android.graphics.Bitmap
import com.king.zxing.util.CodeUtils

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/14
 */
/**
 * 生成二维码
 */
fun String?.createQRCode(
    height: Int = 600
): Bitmap? {
    if (this.isNullOrEmpty()) return null

    return CodeUtils.createQRCode(this, height)
}