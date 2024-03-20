package com.itoys.android.image.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.itoys.android.image.glide.http.OkHttpUrlLoader
import com.itoys.android.image.glide.progress.ProgressManager.glideProgressInterceptor
import okhttp3.OkHttpClient
import java.io.InputStream

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/19
 */
@GlideModule(glideName = "IToysGlide")
class IToysGlideModule : AppGlideModule() {

    companion object {
        /** 把Glide配置方法进行暴露接口 */
        var options: IAppGlideOptions? = null
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        options?.applyOptions(context, builder)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(OkHttpClient.Builder().glideProgressInterceptor().build()))
    }

    override fun isManifestParsingEnabled(): Boolean {
        return options?.isManifestParsingEnabled() ?: false
    }
}