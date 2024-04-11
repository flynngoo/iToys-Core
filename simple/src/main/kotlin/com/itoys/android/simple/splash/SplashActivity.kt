package com.itoys.android.simple.splash

import android.os.Bundle
import androidx.activity.viewModels
import com.itoys.android.core.activity.AbsMviActivity
import com.itoys.android.databinding.SplashActivityLayoutBinding
import com.itoys.android.simple.list.SimpleListActivity
import com.itoys.android.splash.SplashIntent
import com.itoys.android.uikit.components.loading.LottieLoadingDialog
import com.itoys.android.uikit.components.toast.toast
import com.itoys.android.uikit.components.upload.IUploadCallback
import com.itoys.android.utils.expansion.actOpen
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.invalid
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/24
 */
@AndroidEntryPoint
class SplashActivity : AbsMviActivity<SplashActivityLayoutBinding, SplashViewModel>() {

    private val uploadCallback = object : IUploadCallback {
        override fun upload(mark: String) {
            viewModel?.sendIntent(SplashIntent.TestUploadImage(supportFragmentManager, mark))
        }

        override fun delete(mark: String) {
            viewModel?.sendIntent(SplashIntent.TestDeleteImage(supportFragmentManager, mark))
        }
    }

    override val viewModel: SplashViewModel? by viewModels()

    override fun createViewBinding() = SplashActivityLayoutBinding.inflate(layoutInflater)

    override fun initialize(savedInstanceState: Bundle?) {
        binding?.uploadImage?.apply {
            setFragmentManager(supportFragmentManager)
            setUploadImageCallback(uploadCallback)
        }
    }

    override fun addClickListen() {
        super.addClickListen()

        binding?.btnLoading?.doOnClick {
            LottieLoadingDialog.show {
                fm = supportFragmentManager
                closeOnOverlayClick = true
            }.showDialog()
        }

        binding?.btnList?.doOnClick {
            self?.apply { SimpleListActivity::class.actOpen(this) }
        }

        binding?.submit?.doOnClick {
            try {
                binding?.form?.formContent()
            } catch (e: Exception) {
                toast(e.message.invalid())
            }
        }
    }
}