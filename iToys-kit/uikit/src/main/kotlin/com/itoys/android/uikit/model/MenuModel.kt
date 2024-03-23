package com.itoys.android.uikit.model

import androidx.annotation.DrawableRes

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/13
 */
data class MenuModel(
    // 菜单icon
    @DrawableRes val icon: Int,
    // 菜单label
    val label: String,
    // 菜单路由
    val routePath: String,
)
