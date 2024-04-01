package com.itoys.android.simple.datastore

import com.itoys.android.core.datastore.IToysRepository
import com.itoys.android.core.network.BaseEntity
import com.itoys.android.core.network.result
import com.itoys.android.simple.splash.data.SimpleModel
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.util.HashMap

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/26
 */
class SimpleDataSource(
    private val api: SimpleApi
) {

    suspend fun tenantContractList(body: HashMap<String, Any>) = api.tenantContractList(body).result()
}

interface SimpleApi {

    private companion object {

        /** 租客合同 */
        const val TENANT_CONTRACT = "biz-house/rentalContract/rentalContractList"

        /** 业主合同 */
        const val OWNER_CONTRACT = "biz-house/ownerContract/contractList"
    }

    /**
     * 租客合同list
     */
    @GET(TENANT_CONTRACT)
    suspend fun tenantContractList(@QueryMap body: HashMap<String, Any>): BaseEntity<SimpleModel>
}