package com.itoys.android.network.upload.qiniu

import com.itoys.android.network.BaseEntity
import com.itoys.android.network.FilePath
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/1/1
 */
interface QiNiuApi {

    private companion object {

        /** token  */
        const val TOKEN = "blade-system/image-path/token"

        /** path  */
        const val PATH = "blade-system/image-path/set"
    }

    /**
     * 七牛token
     */
    @POST(TOKEN)
    suspend fun token(@Body requestBody: RequestBody): BaseEntity<String>

    @FormUrlEncoded
    @POST(PATH)
    suspend fun path(@Field("path") path: String): BaseEntity<FilePath>
}