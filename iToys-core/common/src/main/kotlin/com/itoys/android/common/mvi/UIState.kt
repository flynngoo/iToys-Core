package com.itoys.android.common.mvi

import com.itoys.android.uikit.components.empty.EmptyStatus
import com.itoys.android.uikit.components.snack.Prompt
import com.itoys.android.uikit.components.toast.ToastyDirection
import com.itoys.android.uikit.components.toast.ToastyStatus

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/23
 */

/**
 * view UI 状态
 */
interface IUIState

/**
 * loading UI 状态
 */
sealed class LoadingUIState {

    data object Idle : LoadingUIState()

    data class Loading(var showLoading: Boolean) : LoadingUIState()

    data class State(val isSuccess: Boolean, val message: String) : LoadingUIState()
}

/**
 * Empty layout UI 状态
 */
sealed class EmptyLayoutUIState {
    data object Idle : EmptyLayoutUIState()

    data class Empty(val status: EmptyStatus) : EmptyLayoutUIState()
}

/**
 * toast UI 状态
 */
sealed class ToastUIState {

    data object Idle : ToastUIState()

    data class Toast(
        val message: String,
        val direction: ToastyDirection = ToastyDirection.Horizontal,
        val status: ToastyStatus? = null
    ) : ToastUIState()

    data class ToastRes(
        val messageRes: Int,
        val direction: ToastyDirection = ToastyDirection.Horizontal,
        val status: ToastyStatus? = null
    ) : ToastUIState()

    data class TopSnack(
        val message: String,
        val withLoading: Boolean = false,
        val prompt: Prompt? = null
    ) : ToastUIState()

    data class BottomSnack(val message: String, val prompt: Prompt? = null) : ToastUIState()
}

sealed class ListUIState : IUIState {

    /**
     * 加载完成
     */
    data class LoadingComplete(val isRefresh: Boolean, val isSuccessful: Boolean): ListUIState()

    /**
     * 没有更多数据
     */
    data object NoMore : ListUIState()

    /**
     * 显示缺省页
     */
    data object ShowEmpty : ListUIState()
}