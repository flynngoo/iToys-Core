package com.itoys.android.uikit.components.loading

import android.view.LayoutInflater
import com.itoys.android.uikit.components.dialog.AbsDialog
import com.itoys.android.uikit.components.dialog.IToysDialog
import com.itoys.android.uikit.databinding.UikitDialogLayoutLoadingBinding

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/31
 */
class LottieLoadingDialog : IToysDialog<UikitDialogLayoutLoadingBinding, LottieLoadingDialog.Builder>() {

    companion object {
        /**
         * 显示对话框
         */
        fun show(builder: Builder.() -> Unit): LottieLoadingDialog {
            val dialog = LottieLoadingDialog()
            dialog.builder = Builder.create().apply(builder)
            return dialog
        }
    }

    /**
     * Dialog builder
     */
    private lateinit var builder: Builder

    override fun createViewBinding(inflater: LayoutInflater) =
        UikitDialogLayoutLoadingBinding.inflate(layoutInflater)

    override fun builder() = builder

    override fun initialize() {
        binding?.uikitLottieLoading?.setAnimation(builder.lottieResource)
        binding?.uikitLottieLoading?.playAnimation()
    }

    class Builder : AbsDialog.Builder() {

        companion object {

            /**
             * 创建 builder
             */
            fun create(): Builder = Builder()
        }

        override var closeOnOverlayClick = false

        /**
         * lottie 资源
         */
        var lottieResource = "loading/loading_paper_airplane.json"
    }
}