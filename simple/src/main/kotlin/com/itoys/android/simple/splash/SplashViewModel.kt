package com.itoys.android.simple.splash

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.itoys.android.R
import com.itoys.android.core.mvi.AbsViewModel
import com.itoys.android.image.IMediaCallback
import com.itoys.android.image.ImageMedia
import com.itoys.android.image.selectFromAlbum
import com.itoys.android.image.takePicture
import com.itoys.android.image.uikit.dialog.ChooseImageDialog
import com.itoys.android.scan.createQRCode
import com.itoys.android.uikit.components.dialog.IDialogCallback
import com.itoys.android.uikit.components.dialog.IToysNoticeDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/24
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
) : AbsViewModel<SplashIntent, SplashState>() {

    override fun init(arguments: Bundle?) {
        super.init(arguments)
        "djqwoidjopqwkpd".createQRCode()
    }

    /**
     * 图片选择回调
     */
    private val mediaCallback by lazy {
        object : IMediaCallback() {
            override fun onResult(result: ImageMedia) {
                super.onResult(result)
                sendUIState(SplashState.UploadImage(result.mediaPath))
            }
        }
    }

    override fun createUIState() = SplashState.Idle

    override fun handlerIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.Next -> sendUIState(navNext())

            is SplashIntent.TestUploadImage -> testUploadImage(intent)

            is SplashIntent.TestDeleteImage -> testDeleteImage(intent)
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        sendIntent(SplashIntent.Next)
    }

    /**
     * 导航到下一个页面
     */
    private fun navNext(): SplashState {
        // 1. 没有登录直接跳转登录
        return SplashState.Login
    }

    /**
     * 测试上传图片
     */
    private fun testUploadImage(intent: SplashIntent.TestUploadImage) {
        ChooseImageDialog.show {
            fm = intent.owner.supportFragmentManager
            callback = object : ChooseImageDialog.ISelectCallback {
                override fun takePicture() {
                    intent.owner.takePicture(callback = mediaCallback)
                }

                override fun selectFromAlbum() {
                    intent.owner.selectFromAlbum(callback = mediaCallback)
                }
            }
        }
    }

    /**
     * 测试删除图片
     */
    private fun testDeleteImage(intent: SplashIntent.TestDeleteImage) {
        IToysNoticeDialog.show {
            fm = intent.owner.supportFragmentManager
            content = "确认删除测试图片吗确认删除测试图片吗确认删除测试图片吗确认删除测试图片吗?"

            callback = object : IDialogCallback() {
                override fun clickCenter() {
                    super.clickCenter()
                    sendUIState(SplashState.DeleteImage)
                }
            }
        }
    }
}