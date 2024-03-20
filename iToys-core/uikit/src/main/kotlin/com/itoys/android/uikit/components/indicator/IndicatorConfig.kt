package com.itoys.android.uikit.components.indicator

import com.itoys.android.uikit.R

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/18
 */
data class IndicatorConfig(
    val textSize: Float = 16f,
    val textTypeface: String = "sans-serif",
    val normalColor: Int = R.color.uikit_colorful_1D2129,
    val selectedColor: Int = R.color.uikit_colorful_42B2F9,
    val selectedTextTypeface: String = "",
    val adjustMode: Boolean = true,
    val withIndicator: Boolean = true,
    val indicatorColor: Int = R.color.uikit_colorful_42B2F9,
) {

    companion object {
        val DEFAULT by lazy { IndicatorConfig() }
    }
}

