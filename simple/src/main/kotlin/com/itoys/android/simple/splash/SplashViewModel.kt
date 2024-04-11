package com.itoys.android.simple.splash

import androidx.lifecycle.LifecycleOwner
import com.itoys.android.core.mvi.AbsViewModel
import com.itoys.android.image.uikit.dialog.ChooseImageDialog
import com.itoys.android.simple.splash.interactor.Simple
import com.itoys.android.splash.SplashIntent
import com.itoys.android.splash.SplashState
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
            fm = intent.fm
            callback = object : ChooseImageDialog.ISelectCallback {
                override fun takePicture() {

                }

                override fun selectFromAlbum() {

                }
            }
        }
    }

    /**
     * 测试删除图片
     */
    private fun testDeleteImage(intent: SplashIntent.TestDeleteImage) {

    }
}