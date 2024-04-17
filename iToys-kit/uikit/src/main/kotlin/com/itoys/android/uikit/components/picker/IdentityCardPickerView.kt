package com.itoys.android.uikit.components.picker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
    }

    /**
     * 设置owner
     */
    fun setOwner(owner: AppCompatActivity) {
        ownerActivity = owner
        binding.portrait.setOwner(owner)
        binding.nationalEmblem.setOwner(owner)
    }

    /**
     * 设置owner
     */
    fun setOwner(owner: Fragment) {
        ownerFragment = owner
        binding.portrait.setOwner(owner)
        binding.nationalEmblem.setOwner(owner)
    }

    /**
     * 设置回调
     */
    fun setUploadImageCallback(uploadImageCallback: IUploadCallback?) {
        binding.portrait.setUploadImageCallback(uploadImageCallback)
        binding.nationalEmblem.setUploadImageCallback(uploadImageCallback)
    }

    /**
     * 添加单张图片
     */
    fun addPictures(picture: String, imageMark: String) {
        when (imageMark) {
            binding.portrait.imageMark() -> {
                binding.portrait.setImage(picture)
            }

            binding.nationalEmblem.imageMark() -> {
                binding.nationalEmblem.setImage(picture)
            }
        }
    }

    /**
     * 设置人像页图片
     */
    fun setPortraitPicture(portraitPicture: String) {
        binding.portrait.setImage(portraitPicture)
    }

    /**
     * 人像页图片
     */
    fun portraitPicture() = binding.portrait.imageUrl()

    /**
     * 设置国徽页图片
     */
    fun setNationalEmblemPicture(nationalEmblemPicture: String) {
        binding.nationalEmblem.setImage(nationalEmblemPicture)

    }

    /**
     * 国徽页图片
     */
    fun nationalEmblemPicture() = binding.nationalEmblem.imageUrl()
}