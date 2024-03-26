package com.itoys.android.core.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/26
 */
abstract class UseCase<out Type, in Params> where Type : Any {

    /**
     * Run.
     */
    abstract suspend fun run(params: Params): Either<Failure, Type>

    operator fun invoke(
        params: Params,
        scope: CoroutineScope = MainScope(),
        onResult: (Either<Failure, Type>) -> Unit = {}
    ) {
        scope.launch {
            val deferredJob = async { run(params) }
            onResult(deferredJob.await())
        }
    }

    /**
     * Helper class to represent Empty
     * Params when a use case does not
     * need them.
     *
     * @see UseCase
     */
    class None
}
