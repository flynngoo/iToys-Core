package com.itoys.android.simple.splash

import androidx.lifecycle.LifecycleOwner
import com.itoys.android.core.mvi.AbsViewModel
import com.itoys.android.simple.splash.interactor.Simple
import com.itoys.android.splash.SplashIntent
import com.itoys.android.splash.SplashState

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/24
 */
class SplashViewModel(
    private val simple: Simple
) : AbsViewModel<SplashIntent, SplashState>() {

    override fun createUIState() = SplashState.Idle

    override fun handlerIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.Next -> sendUIState(navNext())
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        sendIntent(SplashIntent.Next)
    }

    /**
     * 导航到下一个页面
     */
    private fun navNext(): SplashState {
        // 1. 没有登录直接跳转登录
        return SplashState.Login
    }

    private fun testSimple() {
        simple(Simple.Params(1)) {
            handlerAction(
                { result ->

                }
            )
        }
    }
}