package com.itoys.android.simple.splash

import android.os.Bundle
import androidx.activity.viewModels
import com.itoys.android.R
import com.itoys.android.core.activity.AbsMviActivity
import com.itoys.android.core.crash.catchCrash
import com.itoys.android.core.network.handlerException
import com.itoys.android.databinding.SplashActivityLayoutBinding
import com.itoys.android.image.RoundCornerType
import com.itoys.android.image.loadRoundCornerImage
import com.itoys.android.simple.list.SimpleListActivity
import com.itoys.android.uikit.components.dialog.IDialogCallback
import com.itoys.android.uikit.components.dialog.IToysNoticeDialog
import com.itoys.android.uikit.components.form.IFormResultCallback
import com.itoys.android.uikit.components.loading.LottieLoadingDialog
import com.itoys.android.uikit.components.toast.toast
import com.itoys.android.uikit.model.RadioModel
import com.itoys.android.utils.expansion.actOpen
import com.itoys.android.utils.expansion.collect
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.dp2px
import com.itoys.android.utils.expansion.invalid
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

    override val viewModel: SplashViewModel? by viewModels()

    override fun createViewBinding() = SplashActivityLayoutBinding.inflate(layoutInflater)

    /**
     * 测试radio
     */
    private var radioEnable = true

    override fun initialize(savedInstanceState: Bundle?) {
        binding?.idCard?.apply {
            setOwner(this@SplashActivity)
        }

        binding?.simpleImage?.loadRoundCornerImage(
            "http://dayu-img.uc.cn/columbus/img/oc/1002/b36ded73f33bc25e0dc4ec36bf620b0e.jpg",
            radius = 6.dp2px(),
            cornerType = RoundCornerType.DIAGONAL_FROM_TOP_RIGHT
        )

        binding?.textareaForm?.setContent(getString(R.string.uikit_long_text))
        binding?.emoji?.filters = arrayOf(EmojiFilter())
        binding?.amount?.filters = arrayOf(DecimalDigitsInputFilter(2))
        binding?.radio?.setContent(listOf(
            RadioModel(1, 1, "男"),
            RadioModel(2, 2, "女"),
            RadioModel(3, 3, "其他"),
        ))
        binding?.identityCard?.setOwner(this@SplashActivity)
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

        binding?.radioEnable?.doOnClick {
            radioEnable = !radioEnable
            binding?.radio?.setContentEnable(radioEnable)
        }

        binding?.phoneSubmit?.doOnClick {
            catchCrash { binding?.phone?.formContent() }
        }
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