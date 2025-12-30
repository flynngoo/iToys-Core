package com.itoys.android.simple.di

import com.itoys.android.core.network.toApiService
import com.itoys.android.simple.upload.JJQNUpload
import com.itoys.android.simple.upload.JJUploadApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Author Flynn Gu
 * @Email gufanfan.worker@gmail.com
 * @Date 2025/11/20
 */
@Module
@InstallIn(SingletonComponent::class)
class SimpleModule {

    /**
     * 提供 upload api
     */
    @Provides
    @Singleton
    fun provideUploadApi() = JJUploadApi::class.java.toApiService()

    /**
     * 提供 七牛 Upload
     */
    @Provides
    @Singleton
    fun provideJJQNUpload() = JJQNUpload(provideUploadApi())
}