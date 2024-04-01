package com.itoys.android.core.interactor

import android.util.Log
import com.itoys.android.core.network.RequestAction
import com.itoys.android.core.network.handlerException
import com.itoys.android.logcat.asLog
import com.itoys.android.logcat.logcat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/26
 */
abstract class UseCase<Type, P : UseCase.Params> where Type : Any {

    /**
     * Run.
     */
    abstract suspend fun run(params: P): Type?

    operator fun invoke(
        params: P,
        scope: CoroutineScope = MainScope(),
        block: RequestAction<Type>.() -> Unit
    ) {
        val action = RequestAction<Type>().apply(block)

        scope.launch {
            action.start?.invoke(params.showToast())

            flow {
                emit(run(params))
            }.catch { exception ->
                logcat(priority = Log.ERROR) { exception.asLog() }
                action.failure?.invoke(exception.handlerException())
            }.onCompletion {
                action.finish?.invoke()
            }.collect { data -> action.success?.invoke(data) }
        }
    }

    interface Params {
        fun showToast() = true

        fun showLoading() = true
    }
}
