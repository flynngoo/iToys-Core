package com.itoys.android.simple.list

import com.itoys.android.core.mvi.AbsListViewModel
import com.itoys.android.core.mvi.IUIIntent
import com.itoys.android.core.mvi.IUIState
import com.itoys.android.core.mvi.ListUIIntent
import com.itoys.android.logcat.logcat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/6
 */
sealed class SimpleListIntent : IUIIntent

sealed class SimpleListState : IUIState {

    data object Idle : SimpleListState()
}

@HiltViewModel
class SimpleListViewModel @Inject constructor(
) : AbsListViewModel<SimpleListIntent, SimpleListState>() {

    override fun fetchData(intent: ListUIIntent) {
        logcat { "请求数据...." }
    }

    override fun createUIState() = SimpleListState.Idle

    override fun handlerIntent(intent: SimpleListIntent) {
    }
}