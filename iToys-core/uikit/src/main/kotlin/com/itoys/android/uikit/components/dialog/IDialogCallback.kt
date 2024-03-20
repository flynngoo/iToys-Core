package com.itoys.android.uikit.components.dialog

import com.itoys.android.logcat.logcat

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/7
 */
abstract class IDialogCallback {

    /**
     * End button click
     */
    open fun clickStart() {
        logcat { ">>>>> Start button click. <<<<<" }
    }

    /**
     * End button click
     */
    open fun clickCenter() {
        logcat { ">>>>> Center button click. <<<<<" }
    }

    /**
     * End button click
     */
    open fun clickEnd() {
        logcat { ">>>>> End button click. <<<<<" }
    }
}