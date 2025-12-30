package com.itoys.android.uikit.components.form

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/28
 */
data class FormContentConfig(
    val placeholder: String,
    val placeholderColor: Int,
    val contentSize: Int,
    val contentColor: Int,
    @FormTextAlign val contentAlign: Int,
    val maxLength: Int,
    val isEnable: Boolean,
    val enableEmoji: Boolean,
    val enableAmount: Boolean,
    val focusSelection: Boolean,
)
