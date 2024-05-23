package com.itoys.android.simple.form

import com.itoys.android.core.mvi.AbsViewModel
import com.itoys.android.core.mvi.IUIIntent
import com.itoys.android.core.mvi.IUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/5/15
 */
sealed class SimpleFormIntent : IUIIntent

sealed class SimpleFormState : IUIState {

    data object OnInitial : SimpleFormState()
}

@HiltViewModel
class SimpleFormViewModel @Inject constructor(

) : AbsViewModel<SimpleFormIntent, SimpleFormState>() {

    override fun createUIState() = SimpleFormState.OnInitial

    override fun handlerIntent(intent: SimpleFormIntent) {
    }
}