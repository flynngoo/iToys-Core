package com.itoys.android.core.datastore

import android.util.Log
import com.itoys.android.core.network.RequestAction
import com.itoys.android.core.network.handlerException
import com.itoys.android.logcat.asLog
import com.itoys.android.logcat.logcat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/26
 */
abstract class IToysRepository {

    suspend fun <T> request(block: RequestAction<T>.() -> Unit) {
        val action = RequestAction<T>().apply(block)

        flow {
            emit(action.request?.invoke())
        }.flowOn(Dispatchers.IO)
            .catch { exception ->
                logcat(priority = Log.ERROR) { exception.asLog() }
                action.failure?.invoke(exception.handlerException())
            }
            .onCompletion {
                action.finish?.invoke()
            }
            .collect { data -> action.success?.invoke(data) }
    }
}