package com.itoys.android.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.itoys.android.utils.expansion.inputMethodManager

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/13
 */
object InputUtils {

    fun showSoftInput(context: Context, view: View?) {
        context.inputMethodManager?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideSoftInput(activity: Activity) {
        activity.inputMethodManager?.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }
}