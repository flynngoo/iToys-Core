package com.itoys.android.core.network

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itoys.android.logcat.asLog
import com.itoys.android.logcat.logcat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author Fanfan.gu <a href="mailto:fanfan.work@outlook.com">Contact me.</a>
 * @date 18/03/2023
 * @desc
 */

private const val TIME_OUT: Long = 30L

private var retrofit: Retrofit? = null

private val okClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
        .callTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .followRedirects(false)
        .addInterceptor(IToysInterceptor())
        .addInterceptor(
            HttpLoggingInterceptor { message -> logcat { message } }
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .build()
}

fun initRetrofit(apiUrl: String) {
    retrofit = Retrofit.Builder()
        .baseUrl(apiUrl.toHttpUrl())
        .client(okClient)
        .addConverterFactory(JacksonConverterFactory.create(JsonMapper.jsonMapper()))
        .addConverterFactory(MoshiConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
}

fun <T> Class<T>.toApiService(): T {
    if (retrofit == null) throw UninitializedPropertyAccessException("请初始化Retrofit")

    return retrofit!!.create(this)
}

fun <T> BaseEntity<T>?.isSuccessful(successful: Int = ApiResultCode.SUCCESSFUL) = this?.errorCode == successful

fun <T> BaseEntity<T>?.result() : T? {
    if (this.isSuccessful()) {
        return this?.data
    }

    throw ResultException(this?.errorCode ?: 0, this?.msg ?: "请求接口出现异常, 请稍后重试")
}

fun <T> ViewModel.networkRequest(block: RequestAction<T>.() -> Unit) {
    val action = RequestAction<T>().apply(block)

    viewModelScope.launch {
        action.start?.invoke()

        flow {
            emit(action.request?.invoke())
        }.flowOn(Dispatchers.IO)
            .catch { exception ->
                logcat(priority = Log.ERROR) { exception.asLog() }
                action.error?.invoke(exception.handlerException())
            }
            .onCompletion {
                action.finish?.invoke()
            }
            .collect { data -> action.success?.invoke(data) }
    }
}