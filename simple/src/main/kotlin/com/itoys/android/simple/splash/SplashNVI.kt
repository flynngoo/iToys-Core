package com.itoys.android.splash

import com.itoys.android.core.mvi.IUIIntent
import com.itoys.android.core.mvi.IUIState

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/24
 */

sealed class SplashIntent : IUIIntent {

    /**
     * 跳转到下一个页面
     */
    data object Next : SplashIntent()
}

sealed class SplashState : IUIState {

    /**
     * 加载中
     */
    data object Idle : SplashState()

    /**
     * 登录
     */
    data object Login : SplashState()

    /**
     * 主页
     */
    data object Main : SplashState()
}