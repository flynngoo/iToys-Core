package com.itoys.android.simple.upload

import com.itoys.android.core.network.BaseEntity
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/18
 */
interface JJUploadApi {

    private companion object {

        /** token  */
        const val QN_TOKEN = "qiniu/token"
    }

    /**
     * 七牛token
     */
    @POST(QN_TOKEN)
    suspend fun qnToken(@Body requestBody: RequestBody): BaseEntity<QNToken>
}