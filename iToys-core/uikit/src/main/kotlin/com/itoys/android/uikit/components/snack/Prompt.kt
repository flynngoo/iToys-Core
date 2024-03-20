package com.itoys.android.uikit.components.snack

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.itoys.android.uikit.R

/**
 * @author Fanfan.gu <a href="mailto:fanfan.work@outlook.com">Contact me.</a>
 * @date 10/04/2023
 * @desc
 */
enum class Prompt(
    @DrawableRes val icon: Int,
    @ColorRes val backgroundColor: Int,
) {

    SUCCESS(
        R.drawable.uikit_ic_check_white_24dp,
        R.color.uikit_colorful_388E3C
    ),
    ERROR(
        R.drawable.uikit_ic_error_white_24dp,
        R.color.uikit_colorful_D50000
    ),
    WARNING(
        R.drawable.uikit_ic_warning_outline_white_24dp,
        R.color.uikit_colorful_FFA900
    );
}