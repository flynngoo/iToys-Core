package com.itoys.android.uikit.components.picker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.itoys.android.image.DemoImageModel
import com.itoys.android.image.IMediaCallback
import com.itoys.android.image.ImageMedia
import com.itoys.android.image.selectFromAlbum
import com.itoys.android.image.takePicture
import com.itoys.android.image.uikit.dialog.ChooseImageDialog
import com.itoys.android.uikit.R
import com.itoys.android.uikit.components.upload.IUploadCallback
import com.itoys.android.uikit.databinding.UikitLayoutPictureIdentityCardBinding
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isNotBlank

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/26
 */
class IdentityCardPickerView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {

        /** 人像面 */
        const val MARK_FRONT_SIDE = "front"

        val FRONT_DEMO_IMAGE = DemoImageModel(
            imageTitle = "身份证人像面示例",
            image = R.drawable.uikit_img_demo_id_card_front,
        )

        /** 国徽面 */
        const val MARK_BACK_SIDE = "back"

        val BACK_DEMO_IMAGE = DemoImageModel(
            imageTitle = "身份证国徽面示例",
            image = R.drawable.uikit_img_demo_id_card_back,
        )
    }

    private val binding: UikitLayoutPictureIdentityCardBinding by lazy {
        UikitLayoutPictureIdentityCardBinding.inflate(
            LayoutInflater.from(context), this, false
        )
    }

    /**
     * 当前选择图片mark
     */
    private var imageMark = ""

    /**
     * 身份证上传回调
     */
    private val imageUploadCallback by lazy {
        object : IUploadCallback {
            override fun customImageSelection(mark: String) {
                imageMark = mark

                ChooseImageDialog.show {
                    fm = fragmentManager()

                    demoImage = when (mark) {
                        MARK_FRONT_SIDE -> FRONT_DEMO_IMAGE
                        MARK_BACK_SIDE -> BACK_DEMO_IMAGE
                        else -> null
                    }

                    callback = object : ChooseImageDialog.ISelectCallback {
                        override fun selectFromAlbum() {
                            ownerActivity?.selectFromAlbum(callback = mediaCallback)
                            ownerFragment?.selectFromAlbum(callback = mediaCallback)
                        }

                        override fun takePicture() {
                            ownerActivity?.takePicture(callback = mediaCallback)
                            ownerFragment?.takePicture(callback = mediaCallback)
                        }
                    }
                }
            }

            override fun upload(mark: String, path: String) {
            }

            override fun delete(mark: String) {
                uploadCallback?.delete(mark)
            }
        }
    }

    /**
     * 上传回调
     */
    private var uploadCallback: IUploadCallback? = null

    /** 身份证选择回调 */
    private val mediaCallback by lazy {
        object : IMediaCallback() {
            override fun onResult(result: ImageMedia) {
                uploadCallback?.upload(imageMark, result.mediaPath)

                addPictures(imageMark, result.mediaPath)
            }
        }
    }

    /** owner */
    private var ownerActivity: AppCompatActivity? = null

    /** owner */
    private var ownerFragment: Fragment? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        this.addView(binding.root)

        initView(attrs)
    }

    /**
     * 初始化
     */
    private fun initView(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.IdentityCardPickerView)
        val title = ta.getString(R.styleable.IdentityCardPickerView_identityCardTitle).invalid()
        ta.recycle()

        if (title.isNotBlank()) binding.title.text = title
        binding.front.customImageSelection(true)
        binding.front.setUploadImageCallback(imageUploadCallback)
        binding.back.customImageSelection(true)
        binding.back.setUploadImageCallback(imageUploadCallback)
    }

    /**
     * 设置owner
     */
    fun setOwner(owner: AppCompatActivity) {
        ownerActivity = owner
        binding.front.setOwner(owner)
        binding.back.setOwner(owner)
    }

    /**
     * 设置owner
     */
    fun setOwner(owner: Fragment) {
        ownerFragment = owner
        binding.front.setOwner(owner)
        binding.back.setOwner(owner)
    }

    /**
     * 获取fragmentManager
     */
    private fun fragmentManager(): FragmentManager? {
        return ownerFragment?.childFragmentManager ?: ownerActivity?.supportFragmentManager
    }

    /**
     * 设置回调
     */
    fun setUploadImageCallback(uploadImageCallback: IUploadCallback?) {
        uploadCallback = uploadImageCallback
    }

    /**
     * 添加单张图片
     */
    fun addPictures(imageMark: String, picture: String) {
        when (imageMark) {
            binding.front.imageMark() -> {
                // 人像面
                binding.front.setImage(picture)
            }

            binding.back.imageMark() -> {
                // 国徽面
                binding.back.setImage(picture)
            }
        }
    }

    /**
     * 设置图片
     */
    fun setPictures(imageMark: String, picture: String) {
        when (imageMark) {
            binding.front.imageMark() -> setFrontPicture(picture)

            binding.back.imageMark() -> setBackPicture(picture)
        }
    }

    /**
     * 设置人像页图片
     */
    fun setFrontPicture(portraitPicture: String) {
        binding.front.setImage(portraitPicture, needReload = false)
    }

    /**
     * 人像页图片
     */
    fun frontPicture(required: Boolean = true) = binding.front.image(required)

    /**
     * 设置国徽页图片
     */
    fun setBackPicture(nationalEmblemPicture: String) {
        binding.back.setImage(nationalEmblemPicture, needReload = false)

    }

    /**
     * 国徽页图片
     */
    fun backPicture(required: Boolean = true) = binding.back.image(required)
}