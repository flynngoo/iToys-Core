package com.itoys.android.core.interactor

import com.itoys.android.core.network.ResultException

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/26
 */
sealed class Failure {

    /**
     * Network connection error.
     */
    data object NetworkConnection : Failure()

    /**
     * Server error.
     */
    data class ServerError(val err: ResultException) : Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure : Failure()
}