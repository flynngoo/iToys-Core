package com.itoys.android.image.uikit.dialog

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.itoys.android.image.DemoImageModel
import com.itoys.android.image.R
import com.itoys.android.image.databinding.ImageDialogChooseImageBinding
import com.itoys.android.image.loadImage
import com.itoys.android.utils.expansion.gone
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/20
 */
class ChooseImageDialog : DialogFragment() {

    companion object {
        fun show(builder: Builder.() -> Unit) {
            val build = Builder.build().apply(builder)
            val dialog = ChooseImageDialog()
            dialog.setBuilder(build)
            build.fm?.apply { dialog.showDialog(this) }
        }
    }

    private var builder: Builder? = null

    fun setBuilder(builder: Builder) {
        this.builder = builder
    }

    class Builder {
        companion object {
            fun build(): Builder {
                return Builder()
            }
        }

        /**
         * Fragment Manager
         */
        var fm: FragmentManager? = null

        /**
         * 示例图片
         */
        var demoImage: DemoImageModel? = null

        /**
         * 回调
         */
        var callback: ISelectCallback? = null
    }

    private var binding: ImageDialogChooseImageBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ImageDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initDialog()
        return inflater.inflate(R.layout.image_dialog_choose_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ImageDialogChooseImageBinding.bind(view)

        // 标题
        if (builder?.demoImage?.imageTitle.isBlank()) binding?.demoImageTitle?.gone()
        binding?.demoImageTitle?.text = builder?.demoImage?.imageTitle.invalid()

        // 图片
        if (builder?.demoImage?.image == -1) {
            binding?.demoImage?.loadImage(builder?.demoImage?.imageUrl)
        } else {
            binding?.demoImage?.setImageResource(builder?.demoImage?.image ?: 0)
        }
        if (builder?.demoImage?.imageUrl.isBlank() && builder?.demoImage?.image == -1) binding?.demoImage?.gone()

        // 文字
        binding?.demoImageText?.text = builder?.demoImage?.imageText.invalid()
        if (builder?.demoImage?.imageText.isBlank()) binding?.demoImageText?.gone()

        binding?.takePicture?.setOnClickListener {
            builder?.callback?.takePicture()
            dismiss()
        }
        binding?.selectFromAlbum?.setOnClickListener {
            builder?.callback?.selectFromAlbum()
            dismiss()
        }
        binding?.cancel?.setOnClickListener { dismiss() }
        binding?.imageGroup?.setOnClickListener { dismiss() }
    }

    /**
     * 初始化对话框
     */
    private fun initDialog() {
        dialog?.window?.apply {
            // 设置动画
            setWindowAnimations(R.style.ImageDialog_Animation)
            decorView.setPadding(0, 0, 0, 0)
            val params = attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.MATCH_PARENT
            params.gravity = Gravity.BOTTOM
            attributes = params
            isCancelable = true
        }
    }

    /**
     * 显示对话框
     */
    fun showDialog(fm: FragmentManager) {
        show(fm, javaClass.simpleName)
    }

    interface ISelectCallback {

        /**
         * 拍照
         */
        fun takePicture()

        /**
         * 从相册选择
         */
        fun selectFromAlbum()
    }
}