package com.itoys.android.uikit.components.form

import androidx.annotation.IntDef

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/28
 */
@IntDef(FormTextAlign.START, FormTextAlign.CENTER, FormTextAlign.END)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class FormTextAlign {

    companion object {
        const val START = 0
        const val CENTER = 1
        const val END = 2
    }
}