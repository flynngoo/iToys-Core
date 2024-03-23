package com.itoys.android.image.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/19
 */
interface IAppGlideOptions {

    fun isManifestParsingEnabled(): Boolean = false

    fun applyOptions(context: Context, builder: GlideBuilder)

    /**glide 下载进度的主要逻辑 需要在OkHttpClient.Builder().glideProgressInterceptor()**/
    fun registerComponents(context: Context, glide: Glide, registry: Registry)
}