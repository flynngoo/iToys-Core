package com.itoys.android.uikit.components.toast

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.itoys.android.uikit.R

/**
 * @author Fanfan.gu <a href="mailto:fanfan.work@outlook.com">Contact me.</a>
 * @date 13/04/2023
 * @desc Toast status.
 */
enum class ToastyStatus(
    @DrawableRes val icon: Int,
    @ColorRes val tintColor: Int,
    @ColorRes val textColor: Int,
    val withIcon: Boolean,
    val shouldTint: Boolean,
) {

    INFO(
        icon = R.drawable.uikit_ic_info_outline_white_24dp,
        tintColor = R.color.uikit_colorful_3F51B5,
        textColor = R.color.uikit_colorful_white,
        withIcon = true,
        shouldTint = true
    ),

    WARING(
        icon = R.drawable.uikit_ic_warning_outline_white_24dp,
        tintColor = R.color.uikit_colorful_FFA900,
        textColor = R.color.uikit_colorful_white,
        withIcon = true,
        shouldTint = true
    ),

    SUCCESS(
        icon = R.drawable.uikit_ic_check_white_24dp,
        tintColor = R.color.uikit_colorful_388E3C,
        textColor = R.color.uikit_colorful_white,
        withIcon = true,
        shouldTint = true
    ),

    ERROR(
        icon = R.drawable.uikit_ic_error_white_24dp,
        tintColor = R.color.uikit_colorful_D50000,
        textColor = R.color.uikit_colorful_white,
        withIcon = true,
        shouldTint = true
    );
}