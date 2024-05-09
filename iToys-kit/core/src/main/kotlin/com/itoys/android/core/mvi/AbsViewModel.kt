package com.itoys.android.core.mvi

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itoys.android.core.activity.AbsActivity
import com.itoys.android.core.network.RequestAction
import com.itoys.android.core.network.ResultException
import com.itoys.android.core.network.networkRequest
import com.itoys.android.logcat.logcat
import com.itoys.android.utils.expansion.invalid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/23
 */
abstract class AbsViewModel<I : IUIIntent, S : IUIState> : ViewModel(), DefaultLifecycleObserver {

    /** view intent */
    private val _uiIntent: Channel<I> = Channel()
    private val uiIntentFlow: Flow<I> = _uiIntent.receiveAsFlow()

    /** view state */
    private val _uiState: Channel<S> = Channel()
    val uiState: Flow<S> = _uiState.receiveAsFlow()

    /** loading state */
    private val _loadingState: Channel<LoadingUIState> = Channel()
    val loadingState: Flow<LoadingUIState> = _loadingState.receiveAsFlow()

    /** toast state */
    private val _toastState: Channel<ToastUIState> = Channel()
    val toastState: Flow<ToastUIState> = _toastState.receiveAsFlow()

    init {
        launchOnUI {
            uiIntentFlow.collect { intent -> handlerIntent(intent) }
        }
    }

    /**
     * 初始化
     * 会在 @see [AbsActivity.initialize] 之后调用
     */
    open fun init(arguments: Bundle?) {
        // 空实现, 根据页面需求重写此方法
        // 一般会在此处理页面跳转传参数
    }

    /**
     * 创建 view 状态
     */
    protected abstract fun createUIState(): S

    /**
     * 消息分发事件
     */
    protected abstract fun handlerIntent(intent: I)

    /**
     * 在UI线程执行方法
     */
    fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch { block() }
    }

    /**
     * 发送 view 事件
     */
    fun sendIntent(uiIntent: I) {
        launchOnUI { _uiIntent.send(uiIntent) }
    }

    /**
     * 发送 view 状态
     */
    protected open fun sendUIState(block: S) {
        launchOnUI { _uiState.send(block) }
    }

    /**
     * 发送loading
     */
    protected open fun sendLoading(loading: LoadingUIState) {
        launchOnUI { _loadingState.send(loading) }
    }

    /**
     * 发送Toast
     */
    protected open fun sendToast(toast: ToastUIState) {
        launchOnUI { _toastState.send(toast) }
    }

    /**
     * 发送请求
     *
     * [showLoading] 是否显示 loading, 默认显示
     * [showToast] 是否显示 toast, 默认显示
     * [requestTag] 请求tag
     * [success] 请求tag
     * [handleEx] 默认实现, 提示信息由[showToast] 来控制, 如果都不显示, 默认框架提示
     * [request] 请求
     */
    @Deprecated("See UseCase")
    fun <T : Any> launchRequest(
        showLoading: Boolean = false,
        showToast: Boolean = true,
        requestTag: String = UUID.randomUUID().toString(),
        success: ((T?) -> Unit)? = null,
        handleEx: (ResultException) -> Unit = { ex ->
            val msg = ex.msg.invalid("请求出现异常")
            if (showToast) sendToast(ToastUIState.Toast(msg))
        },
        request: suspend () -> T?,
    ) {
        networkRequest {
            start {
                logcat(priority = Log.INFO) { "Request(${requestTag}) is start!" }
                sendLoading(LoadingUIState.Loading(showLoading))
            }
            request { request() }
            success { result -> success?.invoke(result) }
            failure { exception -> handleEx.invoke(exception) }
            finish {
                logcat(priority = Log.INFO) { "Request(${requestTag}) is finish!" }
                if (showLoading) sendLoading(LoadingUIState.Loading(showLoading = false))
            }
        }
    }

    /**
     * 封装请求action
     */
    fun <T> RequestAction<T>.handlerAction(
        success: (T?) -> Unit,
        failure: (ResultException) -> Unit = ::handleFailure
    ) {
        start { sendLoading(LoadingUIState.Loading(it)) }
        success(success)
        failure(failure)
        finish { sendLoading(LoadingUIState.Loading(showLoading = false)) }
    }

    /**
     * 封装请求action
     */
    fun <T> RequestAction<T>.handlerAction(
        start: (Boolean) -> Unit,
        success: (T?) -> Unit,
        failure: (ResultException) -> Unit = ::handleFailure
    ) {
        start(start)
        success(success)
        failure(failure)
        finish { sendLoading(LoadingUIState.Loading(showLoading = false)) }
    }

    /**
     * 默认处理请求失败
     */
    open fun handleFailure(failure: ResultException) {
        val msg = failure.msg.invalid("请求出现异常")
        sendToast(ToastUIState.Toast(msg))
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
        _uiIntent.close()
    }
}