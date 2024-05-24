package com.itoys.android.core.network

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itoys.android.core.CoreConfig
import com.itoys.android.logcat.asLog
import com.itoys.android.logcat.logcat
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.size
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

fun <T> BaseEntity<T>?.isSuccessful(successful: Int = NetworkInitialization.requireApiSuccessfulCode()) = this?.errorCode == successful

fun <T> BaseListEntity<T>?.isSuccessful(successful: Int = NetworkInitialization.requireApiSuccessfulCode()) = this?.errorCode == successful

fun <T> BasePageEntity<T>?.isSuccessful(successful: Int = NetworkInitialization.requireApiSuccessfulCode()) = this?.errorCode == successful

fun <T> BaseEntity<T>?.result() : T? {
    if (this.isSuccessful()) {
        return this?.data
    }

    val errorMsg = if (this?.msg.isBlank() || this?.msg.size() > 100) {
        CoreConfig.defaultApiErrorMsg
    } else {
        this?.msg
    }

    throw ResultException(this?.errorCode ?: 0, errorMsg)
}

fun <T> BaseListEntity<T>?.result() : List<T>? {
    if (this.isSuccessful()) {
        return this?.data
    }

    val errorMsg = if (this?.msg.isBlank() || this?.msg.size() > 100) {
        CoreConfig.defaultApiErrorMsg
    } else {
        this?.msg
    }

    throw ResultException(this?.errorCode ?: 0, errorMsg)
}

fun <T> BasePageEntity<T>?.result() : PageEntity<T>? {
    if (this.isSuccessful()) {
        return this?.data
    }

    val errorMsg = if (this?.msg.isBlank() || this?.msg.size() > 100) {
        CoreConfig.defaultApiErrorMsg
    } else {
        this?.msg
    }

    throw ResultException(this?.errorCode ?: 0, errorMsg)
}

/**
 * 统一异常处理
 */
fun String.asThrowable(code: Int = ApiResultCode.PARAMS_ERROR) {
    throw ResultException(code, this)
}

fun <T> ViewModel.networkRequest(block: RequestAction<T>.() -> Unit) {
    val action = RequestAction<T>().apply(block)

    viewModelScope.launch {
        action.start?.invoke(true)

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