package com.itoys.android.common.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.itoys.android.common.mvi.AbsViewModel
import com.itoys.android.common.mvi.EmptyLayoutUIState
import com.itoys.android.common.mvi.IUIIntent
import com.itoys.android.common.mvi.IUIState
import com.itoys.android.common.mvi.ToastUIState
import com.itoys.android.uikit.components.snack.TopSnackBar
import com.itoys.android.uikit.components.snack.makeSnack
import com.itoys.android.uikit.components.toast.toast
import kotlinx.coroutines.launch

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/30
 */
abstract class AbsMviFragment<VB : ViewBinding, VM : AbsViewModel<out IUIIntent, out IUIState>> : AbsFragment<VB>() {

    /** view model */
    abstract val viewModel: VM?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObserver()
    }

    override fun initData() {
        viewModel?.init(arguments)
    }

    /**
     * 添加观察者, 注册监听
     */
    protected open fun addObserver() {
        // 添加观察者, 注册监听
        viewModel?.let { viewModel ->
            lifecycle.addObserver(viewModel)

            lifecycleScope.launch { uiCollect() }
            lifecycleScope.launch { toastStateObserver(viewModel) }
            lifecycleScope.launch { stateLayoutObserver(viewModel) }
        }
    }

    /**
     * toast 状态订阅
     */
    private suspend fun toastStateObserver(viewModel: VM) {
        viewModel.toastState.collect { state ->
            when (state) {
                is ToastUIState.Toast -> {
                    toast(state.message, status = state.status)
                }

                is ToastUIState.ToastRes -> {
                    toast(
                        message = getString(state.messageRes),
                        direction = state.direction,
                        status = state.status
                    )
                }

                is ToastUIState.TopSnack -> {
                    makeSnack(
                        state.message,
                        appearDirection = TopSnackBar.APPEAR_FROM_TOP_TO_DOWN,
                        withLoading = state.withLoading,
                        prompt = state.prompt
                    )
                }

                is ToastUIState.BottomSnack -> {
                    makeSnack(
                        state.message,
                        appearDirection = TopSnackBar.APPEAR_FROM_BOTTOM_TO_TOP,
                    )
                }

                else -> {
                    /* 空实现 */
                }
            }
        }
    }

    /**
     * state layout 状态订阅
     */
    private suspend fun stateLayoutObserver(viewModel: VM) {
        viewModel.emptyState.collect { state ->
            when (state) {
                is EmptyLayoutUIState.Empty -> {
                    emptyLayout?.showStatus(state.status)
                }

                else -> {
                    /* 空实现 */
                }
            }
        }
    }

    /**
     * UI 状态更新
     */
    abstract suspend fun uiCollect()
}