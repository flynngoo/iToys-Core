package com.itoys.android.simple.repository

import com.itoys.android.core.datastore.IToysRepository
import com.itoys.android.simple.datastore.SimpleDataSource
import com.itoys.android.simple.splash.data.SimpleModel

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/26
 */
interface SimpleRepository {

    suspend fun simple(): SimpleModel?

    class Network(
        private val simpleDataSource: SimpleDataSource
    ) : IToysRepository(), SimpleRepository {
        override suspend fun simple(): SimpleModel? = simpleDataSource.tenantContractList(hashMapOf("pageNum" to "1", "pageSize" to "10"))
    }
}