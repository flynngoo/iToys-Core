package com.itoys.android.simple.splash

import android.os.Bundle
import androidx.activity.viewModels
import com.itoys.android.R
import com.itoys.android.core.activity.AbsMviActivity
import com.itoys.android.databinding.SplashActivityLayoutBinding
import com.itoys.android.image.RoundCornerType
import com.itoys.android.image.loadRoundCornerImage
import com.itoys.android.simple.list.SimpleListActivity
import com.itoys.android.splash.SplashIntent
import com.itoys.android.splash.SplashState
import com.itoys.android.uikit.components.dialog.IDialogCallback
import com.itoys.android.uikit.components.dialog.IToysNoticeDialog
import com.itoys.android.uikit.components.form.IFormResultCallback
import com.itoys.android.uikit.components.loading.LottieLoadingDialog
import com.itoys.android.uikit.components.toast.toast
import com.itoys.android.uikit.components.upload.IUploadCallback
import com.itoys.android.utils.expansion.actOpen
import com.itoys.android.utils.expansion.collect
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.dp2px
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isNotBlank
import com.itoys.android.utils.expansion.toCNY
import com.itoys.android.utils.filter.DecimalDigitsInputFilter
import com.itoys.android.utils.filter.EmojiFilter
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
            viewModel?.sendIntent(SplashIntent.TestUploadImage(this@SplashActivity, mark))
        }

        override fun delete(mark: String) {
            viewModel?.sendIntent(SplashIntent.TestDeleteImage(this@SplashActivity, mark))
        }
    }

    override val viewModel: SplashViewModel? by viewModels()

    override fun createViewBinding() = SplashActivityLayoutBinding.inflate(layoutInflater)

    override fun initialize(savedInstanceState: Bundle?) {
        binding?.idCard?.apply {
            setFragmentManager(supportFragmentManager)
            setUploadImageCallback(uploadCallback)
        }

        binding?.simpleImage?.loadRoundCornerImage(
            "http://dayu-img.uc.cn/columbus/img/oc/1002/b36ded73f33bc25e0dc4ec36bf620b0e.jpg",
            radius = 6.dp2px(),
            cornerType = RoundCornerType.DIAGONAL_FROM_TOP_RIGHT
        )

        binding?.textareaForm?.setContent(getString(R.string.uikit_long_text))
        binding?.emoji?.filters = arrayOf(EmojiFilter())
        binding?.amount?.filters = arrayOf(DecimalDigitsInputFilter(2))
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
            IToysNoticeDialog.show {
                fm = supportFragmentManager
                title = "这是标题这是标题这是标题这是标题"
                content = "确认删除测试图片吗确认删除测试图片吗确认删除测试图片吗确认删除测试图片吗?"

                callback = object : IDialogCallback() {
                    override fun clickCenter() {
                        super.clickCenter()
                    }
                }
            }
        }

        binding?.shippingCost?.setResultCallback(object : IFormResultCallback() {
            override fun result(result: String) {
                super.result(result)


            }
        })
    }

    override fun addObserver() {
        super.addObserver()
        viewModel?.apply { collect(uiState, ::uiCollect) }
    }

    /**
     * UI 状态
     */
    private fun uiCollect(state: SplashState?) {
        when (state) {
            is SplashState.UploadImage -> binding?.idCard?.setImage(state.imageUrl)

            is SplashState.DeleteImage -> binding?.idCard?.deleteImage()

            else -> {}
        }
    }
}