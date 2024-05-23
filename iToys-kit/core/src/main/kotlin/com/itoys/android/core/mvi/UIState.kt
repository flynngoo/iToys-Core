package com.itoys.android.core.mvi

import com.itoys.android.core.network.ResultException
import com.itoys.android.uikit.components.snack.Prompt
import com.itoys.android.uikit.components.toast.ToastyOrientation
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

    data class Loading(var showLoading: Boolean) : LoadingUIState()

    data class State(val isSuccess: Boolean, val message: String) : LoadingUIState()
}

/**
 * toast UI 状态
 */
sealed class ToastUIState {

    data class Toast(
        val message: String,
        val orientation: ToastyOrientation = ToastyOrientation.Horizontal,
        val status: ToastyStatus? = null
    ) : ToastUIState()

    data class ToastRes(
        val messageRes: Int,
        val orientation: ToastyOrientation = ToastyOrientation.Horizontal,
        val status: ToastyStatus? = null
    ) : ToastUIState()

    data class TopSnack(
        val message: String,
        val withLoading: Boolean = false,
        val prompt: Prompt? = null
    ) : ToastUIState()

    data class BottomSnack(val message: String, val prompt: Prompt? = null) : ToastUIState()
}

/**
 * 错误状态回调
 */
sealed class FailureUIState {

    data class Failure(val throwable: ResultException) : FailureUIState()
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