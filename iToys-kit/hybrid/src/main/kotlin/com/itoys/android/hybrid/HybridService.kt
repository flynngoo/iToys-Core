package com.itoys.android.hybrid

import android.graphics.Bitmap
import com.itoys.android.utils.expansion.empty
import com.viatris.hybrid.annotation.NO_SUPPORT
import com.viatris.hybrid.callback.JSCallBack
import com.viatris.hybrid.model.AlertDialogBean
import com.viatris.hybrid.model.ImagePreviewBean
import com.viatris.hybrid.model.LocationBean
import com.viatris.hybrid.model.ShareInfoBean
import com.viatris.hybrid.model.UserBean
import com.viatris.hybrid.service.ViaJSService

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/5/9
 */
class HybridService : ViaJSService() {
    override fun getBitmap(p0: String?): Bitmap {
        TODO("Not yet implemented")
    }

    override fun login(callback: JSCallBack?) {
        callback?.onError(NO_SUPPORT, "服务暂不支持！")
    }

    override fun logout(data: String?, callback: JSCallBack?) {
        callback?.onError(NO_SUPPORT, "服务暂不支持！")
    }

    override fun dialog(bean: AlertDialogBean?, callback: JSCallBack?) {
        callback?.onError(NO_SUPPORT, "服务暂不支持！")
    }

    override fun share(bean: Array<out ShareInfoBean>?, callback: JSCallBack?) {
        callback?.onError(NO_SUPPORT, "服务暂不支持！")
    }

    override fun imagePreview(image: ImagePreviewBean?, callback: JSCallBack?) {
        callback?.onError(NO_SUPPORT, "服务暂不支持！")
    }

    override fun getUserBean(): UserBean {
        return UserBean()
    }

    override fun getLocation(): LocationBean {
        return LocationBean(String.empty(), -1f, -1f)
    }

    override fun clearTop(count: Int, callback: JSCallBack?) {
        callback?.onError(NO_SUPPORT, "服务暂不支持！")
    }
}