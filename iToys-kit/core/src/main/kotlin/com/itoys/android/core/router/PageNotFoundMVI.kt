package com.itoys.android.core.router

import android.os.Bundle
import com.itoys.android.core.mvi.AbsViewModel
import com.itoys.android.core.mvi.IUIIntent
import com.itoys.android.core.mvi.IUIState

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/3
 */

sealed class PageNotFoundIntent : IUIIntent {

    /**
     * 页面跳转
     */
    data object Navigation : PageNotFoundIntent()
}

sealed class PageNotFoundState : IUIState {

    data object Idle : PageNotFoundState()
}

class PageNotFoundViewModel : AbsViewModel<PageNotFoundIntent, PageNotFoundState>() {

    /** 页面地址 */
    private var pageAddress = ""

    override fun init(arguments: Bundle?) {
        super.init(arguments)
        pageAddress = arguments?.getString(ArgsKeys.PAGE_ADDRESS) ?: ""
    }

    override fun createUIState() = PageNotFoundState.Idle

    override fun handlerIntent(intent: PageNotFoundIntent) {
        when (intent) {
            is PageNotFoundIntent.Navigation -> pageAddress.navigation()
        }
    }
}