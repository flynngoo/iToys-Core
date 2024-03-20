package com.itoys.android.image.glide

import android.content.Context
import android.widget.ImageView
import com.itoys.android.image.loadImage
import com.itoys.android.image.loadResizeImage
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.utils.ActivityCompatHelper

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/20
 */
object GlideImageEngine : ImageEngine {

    override fun loadImage(context: Context?, url: String?, imageView: ImageView?) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }

        imageView?.loadImage(url)
    }

    override fun loadImage(
        context: Context?,
        imageView: ImageView?,
        url: String?,
        maxWidth: Int,
        maxHeight: Int
    ) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }

        imageView?.loadResizeImage(url, maxWidth, maxHeight)
    }

    override fun loadAlbumCover(context: Context?, url: String?, imageView: ImageView?) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }

        imageView?.loadResizeImage(url, 180, 180)
    }

    override fun loadGridImage(context: Context?, url: String?, imageView: ImageView?) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }

        imageView?.loadResizeImage(url, 200, 200)
    }

    override fun pauseRequests(context: Context?) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }

        context?.apply { IToysGlide.with(context).pauseRequests() }
    }

    override fun resumeRequests(context: Context?) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }

        context?.apply { IToysGlide.with(context).resumeRequests() }
    }
}