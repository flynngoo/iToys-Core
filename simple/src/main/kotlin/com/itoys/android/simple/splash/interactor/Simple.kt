package com.itoys.android.simple.splash.interactor

import com.itoys.android.core.interactor.UseCase
import com.itoys.android.simple.repository.SimpleRepository
import com.itoys.android.simple.splash.data.SimpleModel

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/26
 */
class Simple(
    private val repository: SimpleRepository
) : UseCase<SimpleModel, Simple.Params>() {

    override suspend fun run(params: Params) = repository.simple()

    data class Params(
        val id: Int?
    ) : UseCase.Params {
        override fun showToast() = false
    }
}