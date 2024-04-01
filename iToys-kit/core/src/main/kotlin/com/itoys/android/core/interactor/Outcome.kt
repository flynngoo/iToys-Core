package com.itoys.android.core.interactor

import com.itoys.android.core.network.ResultException

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/26
 */
sealed class Outcome<Type> {

    data object Start : Outcome<None>()

    data class Success<Type>(val data: Type) : Outcome<Type>()

    data class Failure(val e: ResultException) : Outcome<None>()

    data object Finish : Outcome<None>()

    /**
     * Helper class to represent Empty
     * Params when a use case does not
     * need them.
     *
     * @see UseCase
     */
    class None
}