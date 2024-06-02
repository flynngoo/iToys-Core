package com.itoys.android.simple.form

import android.os.Bundle
import androidx.activity.viewModels
import com.itoys.android.R
import com.itoys.android.core.activity.AbsMviActivity
import com.itoys.android.databinding.SimpleActivityFormBinding
import com.itoys.android.image.DemoImageModel
import com.itoys.android.image.IMediaCallback
import com.itoys.android.image.ImageMedia
import com.itoys.android.image.selectFromAlbum
import com.itoys.android.image.takePicture
import com.itoys.android.image.uikit.dialog.ChooseImageDialog
import com.itoys.android.uikit.components.form.IFormResultCallback
import com.itoys.android.uikit.components.picker.IdentityCardPickerView
import com.itoys.android.uikit.components.upload.IUploadCallback
import com.itoys.android.utils.expansion.safeParseDouble
import com.itoys.android.utils.expansion.stripTrailingZeros
import com.itoys.android.utils.filter.EmojiFilter
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/5/15
 */
@AndroidEntryPoint
class SimpleFormActivity : AbsMviActivity<SimpleActivityFormBinding, SimpleFormViewModel>() {

    /**
     * 总运费
     */
    private var shippingCost = 0.0

    /**
     * 现金运费
     */
    private var cashShippingCost = 0.0

    /**
     * 万金油油费
     */
    private var gasCost = 0.0

    /** 图片选择回调 */
    private val mediaCallback by lazy {
        object : IMediaCallback() {
            override fun onResult(result: ImageMedia) {
                binding?.identityCard?.addPictures(IdentityCardPickerView.MARK_FRONT_SIDE, result.mediaPath)
            }
        }
    }

    /**
     * 图片上传回调
     */
    private val imageUploadCallback by lazy {
        object : IUploadCallback {
            override fun customImageSelection(mark: String) {
                ChooseImageDialog.show {
                    fm = supportFragmentManager
                    demoImage = DemoImageModel(
                        imageTitle = "身份证人像页示例",
                        image = R.drawable.uikit_img_demo_id_card_front
                    )

                    callback = object : ChooseImageDialog.ISelectCallback {
                        override fun selectFromAlbum() { selectFromAlbum(callback = mediaCallback) }

                        override fun takePicture() { takePicture(callback = mediaCallback) }
                    }
                }
            }

            override fun upload(mark: String, path: String) {

            }
        }
    }

    override val viewModel: SimpleFormViewModel? by viewModels()

    override fun createViewBinding() = SimpleActivityFormBinding.inflate(layoutInflater)

    override fun initialize(savedInstanceState: Bundle?) {
        binding?.identityCard?.setOwner(this)
        binding?.other?.setOwner(this)
        binding?.emoji?.filters = arrayOf(EmojiFilter())
    }

    override fun addClickListen() {
        super.addClickListen()
        binding?.cost?.setResultCallback(object : IFormResultCallback() {
            override fun result(result: String) {
                super.result(result)
                shippingCost = result.safeParseDouble()

                calculateCashCost()
                calculateGasCost()
            }
        })

        binding?.cashCost?.setResultCallback(object : IFormResultCallback() {
            override fun result(result: String) {
                super.result(result)
                cashShippingCost = result.safeParseDouble()
                calculateGasCost()
            }
        })

        binding?.gasCost?.setResultCallback(object : IFormResultCallback() {
            override fun result(result: String) {
                super.result(result)
                gasCost = result.safeParseDouble()
                calculateCashCost()
            }
        })

        binding?.identityCard?.setUploadImageCallback(imageUploadCallback)
    }

    /**
     * 计算现金运费
     */
    private fun calculateCashCost() {
        cashShippingCost = shippingCost.minus(gasCost)
        if (cashShippingCost < 0) cashShippingCost = 0.0
        binding?.cashCost?.setContent(cashShippingCost.stripTrailingZeros())
    }

    /**
     * 计算万金油运费
     */
    private fun calculateGasCost() {
        gasCost = shippingCost.minus(cashShippingCost)
        if (gasCost < 0) gasCost = 0.0
        binding?.gasCost?.setContent(gasCost.stripTrailingZeros())
    }
}