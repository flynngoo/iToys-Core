package com.itoys.android.uikit.components.form

import androidx.annotation.IntDef

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/28
 */
@IntDef(
    FormModel.TEXT,
    FormModel.NUMBER,
    FormModel.MOBILE,
    FormModel.ID_CARD,
    FormModel.EMAIL,
    FormModel.ADDRESS,
    FormModel.DATE,
    FormModel.DATETIME,
    FormModel.SELECT,
    FormModel.RADIO
)
@Retention(AnnotationRetention.SOURCE)
annotation class FormModel {

    companion object {
        const val TEXT = 0
        const val NUMBER = 1
        const val MOBILE = 2
        const val ID_CARD = 3
        const val EMAIL = 4
        const val ADDRESS = 5
        const val DATE = 6
        const val DATETIME = 7
        const val SELECT = 8
        const val RADIO = 9
    }
}