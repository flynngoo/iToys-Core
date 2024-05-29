package com.itoys.android.uikit.components.picker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.itoys.android.image.DemoImageModel
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

    private val binding: UikitLayoutPictureIdentityCardBinding

    /** owner */
    private var ownerActivity: AppCompatActivity? = null

    /** owner */
    private var ownerFragment: Fragment? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        binding = UikitLayoutPictureIdentityCardBinding.inflate(
            LayoutInflater.from(context), this, false
        )
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
        binding.back.customImageSelection(true)
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
     * 设置回调
     */
    fun setUploadImageCallback(uploadImageCallback: IUploadCallback?) {
        binding.front.setUploadImageCallback(uploadImageCallback)
        binding.back.setUploadImageCallback(uploadImageCallback)
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
     * 设置人像页图片
     */
    fun setFrontPicture(portraitPicture: String) {
        binding.front.setImage(portraitPicture, needReload = false)
    }

    /**
     * 人像页图片
     */
    fun frontPicture() = binding.front.image(required = true)

    /**
     * 设置国徽页图片
     */
    fun setBackPicture(nationalEmblemPicture: String) {
        binding.back.setImage(nationalEmblemPicture, needReload = false)

    }

    /**
     * 国徽页图片
     */
    fun backPicture() = binding.back.image(required = true)
}