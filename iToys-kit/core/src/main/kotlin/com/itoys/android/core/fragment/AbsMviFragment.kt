package com.itoys.android.core.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.itoys.android.core.mvi.AbsViewModel
import com.itoys.android.core.mvi.IUIIntent
import com.itoys.android.core.mvi.IUIState
import com.itoys.android.core.mvi.ToastUIState
import com.itoys.android.uikit.components.snack.TopSnackBar
import com.itoys.android.uikit.components.snack.makeSnack
import com.itoys.android.uikit.components.toast.toast
import com.itoys.android.utils.expansion.collect
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
        }

        viewModel?.apply { collect(toastState, ::toast) }
    }

    /**
     * toast
     */
    private fun toast(toast: ToastUIState?) {
        when (toast) {
            is ToastUIState.Toast -> {
                toast(toast.message, direction = toast.direction, status = toast.status)
            }

            is ToastUIState.ToastRes -> {
                toast(
                    message = getString(toast.messageRes),
                    direction = toast.direction,
                    status = toast.status
                )
            }

            is ToastUIState.TopSnack -> {
                makeSnack(
                    toast.message,
                    appearDirection = TopSnackBar.APPEAR_FROM_TOP_TO_DOWN,
                    withLoading = toast.withLoading,
                    prompt = toast.prompt
                )
            }

            is ToastUIState.BottomSnack -> {
                makeSnack(
                    toast.message,
                    appearDirection = TopSnackBar.APPEAR_FROM_BOTTOM_TO_TOP,
                )
            }

            else -> {
                /* 空实现 */
            }
        }
    }

    /**
     * UI 状态更新
     */
    abstract suspend fun uiCollect()
}