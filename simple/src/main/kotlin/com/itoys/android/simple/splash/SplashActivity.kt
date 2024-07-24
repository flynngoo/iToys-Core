package com.itoys.android.simple.splash

import android.os.Bundle
import androidx.activity.viewModels
import com.itoys.android.core.activity.AbsMviActivity
import com.itoys.android.core.crash.catchCrash
import com.itoys.android.databinding.SplashActivityLayoutBinding
import com.itoys.android.hybrid.HybridRouter
import com.itoys.android.logcat.logcat
import com.itoys.android.simple.form.SimpleFormActivity
import com.itoys.android.simple.list.SimpleListActivity
import com.itoys.android.uikit.components.picker.DatetimePicker
import com.itoys.android.uikit.components.toast.toast
import com.itoys.android.uikit.components.upgrade.UpgradeDialog
import com.itoys.android.uikit.components.upgrade.UpgradeModel
import com.itoys.android.uikit.components.upgrade.filename
import com.itoys.android.uikit.components.upload.IUploadCallback
import com.itoys.android.uikit.model.RadioModel
import com.itoys.android.utils.PathUtils
import com.itoys.android.utils.expansion.actOpen
import com.itoys.android.utils.expansion.collect
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.md5
import com.itoys.android.utils.expansion.padZeroes
import com.itoys.android.utils.expansion.toFile
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
     * 上传图片
     */
    private var uploadEnable = false

    /**
     * 测试radio
     */
    private var radioEnable = true

    override fun initialize(savedInstanceState: Bundle?) {
        binding?.idCard?.apply {
            setOwner(this@SplashActivity)

            setUploadImageCallback(object : IUploadCallback {
                override fun upload(mark: String, path: String) {
                }
            })
        }

        binding?.radio?.setContent(listOf(
            RadioModel(1, 1, "男"),
            RadioModel(2, 2, "女"),
            RadioModel(3, 3, "其他"),
        ))
    }

    override fun addClickListen() {
        super.addClickListen()
        binding?.btnLoading?.doOnClick {
            self?.apply { HybridRouter.navHybrid("https://www.baidu.com") }
        }

        binding?.btnList?.doOnClick {
            self?.apply { SimpleListActivity::class.actOpen(this) }
        }

        binding?.idCardBtn?.doOnClick {
            uploadEnable = !uploadEnable
            binding?.idCard?.setUploadEnable(uploadEnable)
        }

        binding?.noticeDialog?.doOnClick {
            /* IToysNoticeDialog.show {
                fm = supportFragmentManager
                title = "这是标题这是标题这是标题这是标题"
                content = "确认删除测试图片吗确认删除测试图片吗确认删除测试图片吗确认删除测试图片吗?"

                callback = object : IDialogCallback() {
                    override fun clickCenter() {
                        super.clickCenter()
                    }
                }
            } */

            /* DatetimePicker.show {
                fm = supportFragmentManager
                fromToday = false

                callback = DatetimePicker.IDatetimeCallback { year, month, day, hour, minute, second ->
                    toast("$year-${month.padZeroes()}-${day.padZeroes()} ${hour.padZeroes()}:${minute.padZeroes()}:${second.padZeroes()}")
                }
            } */
            UpgradeDialog.show {
                fm = supportFragmentManager
                upgradeData = UpgradeModel(
                    isIgnorable = true,
                    versionCode = 32017,
                    versionName = "3.2.17",
                    upgradeLog = "小车订单审核前允许修改车辆价值",
                    apkUrl = "http://img.bjjjst.com/00000000app/site/v3.1.18/com.mykj.jingji.site-20240618205454-v3.1.18.apk",
                    apkSize = "25",
                    md5 = "d41d8cd98f00b204e9800998ecf8427e"
                )
            }
        }

        binding?.form?.doOnClick { SimpleFormActivity::class.actOpen(this) }

        binding?.simpleImage?.doOnClick {
        }

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