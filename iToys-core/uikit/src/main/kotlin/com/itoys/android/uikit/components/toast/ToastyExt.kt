package com.itoys.android.uikit.components.toast

import androidx.annotation.StringRes
import com.itoys.android.uikit.UikitInitialization
import com.itoys.android.utils.UtilsInitialization

fun toast(
    @StringRes message: Int,
    duration: Int = Toasty.LENGTH_SHORT,
    direction: ToastyDirection = ToastyDirection.Horizontal,
    status: ToastyStatus? = null
) {
    if (status == null) {
        Toasty.normal(
            UtilsInitialization.requireApp(),
            UikitInitialization.requireApp().getString(message),
            duration = duration,
            direction = direction
        ).show()
    } else {
        Toasty.custom(
            UtilsInitialization.requireApp(),
            UikitInitialization.requireApp().getString(message),
            duration = duration,
            toastyDirection = direction,
            toastyStatus = status
        ).show()
    }
}

fun toast(
    message: String,
    duration: Int = Toasty.LENGTH_SHORT,
    direction: ToastyDirection = ToastyDirection.Horizontal,
    status: ToastyStatus? = null
) {
    if (status == null) {
        Toasty.normal(
            UtilsInitialization.requireApp(),
            message,
            duration = duration,
            direction = direction
        ).show()
    } else {
        Toasty.custom(
            UtilsInitialization.requireApp(),
            message,
            duration = duration,
            toastyDirection = direction,
            toastyStatus = status
        ).show()
    }
}
