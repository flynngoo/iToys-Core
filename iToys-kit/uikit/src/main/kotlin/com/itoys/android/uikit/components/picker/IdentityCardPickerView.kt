package com.itoys.android.uikit.components.picker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentManager
import com.itoys.android.image.loadRoundCornerImage
import com.itoys.android.image.uikit.dialog.SelectPictureDialog
import com.itoys.android.uikit.R
import com.itoys.android.uikit.components.dialog.IDialogCallback
import com.itoys.android.uikit.components.dialog.IToysNoticeDialog
import com.itoys.android.uikit.databinding.UikitLayoutPictureIdentityCardBinding
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.dp2px
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank
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
        /** 人像 */
        const val PORTRAIT = 0x00

        const val KEY_PORTRAIT = "portrait"

        /** 国徽 */
        const val NATIONAL_EMBLEM = 0x01

        const val KEY_NATIONAL_EMBLEM = "national_emblem"
    }

    /** 选择图片index */
    private var pictureIndex = PORTRAIT

    /** 人像 */
    private var portrait: AppCompatImageView? = null

    /** 人像删除 */
    private var deletePortrait: AppCompatImageView? = null

    /** 人像图片 */
    private var portraitPicture = ""

    /** 国徽 */
    private var nationalEmblem: AppCompatImageView? = null

    /** 国徽删除 */
    private var deleteNationalEmblem: AppCompatImageView? = null

    /** 国徽图片 */
    private var nationalEmblemPicture = ""

    /** fm */
    private var fragmentManager: FragmentManager? = null

    /** 图片选择回调 */
    private var pictureCallback: SelectPictureDialog.ISelectCallback? = null

    /** 身份证选择回调 */
    private var identityCardCallback: IIdentityCardCallback? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        initView(attrs)
    }

    /**
     * 初始化
     */
    private fun initView(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.IdentityCardPickerView)
        val title = ta.getString(R.styleable.IdentityCardPickerView_identityCardTitle).invalid()
        ta.recycle()

        val binding = UikitLayoutPictureIdentityCardBinding.inflate(
            LayoutInflater.from(context), this, false
        )
        this.addView(binding.root)

        if (title.isNotBlank()) binding.title.text = title

        // 人像页
        portrait = binding.imgPortrait
        deletePortrait = binding.deletePortrait
        binding.portrait.doOnClick {
            pictureIndex = PORTRAIT

            SelectPictureDialog.show {
                fm = fragmentManager
                callback = pictureCallback
            }
        }

        binding.deletePortrait.doOnClick {
            IToysNoticeDialog.show {
                this.title = context.getString(R.string.uikit_id_card_delete_portrait)
                fm = fragmentManager

                buttons = arrayOf(
                    context.getString(R.string.uikit_cancel),
                    context.getString(R.string.uikit_confirm),
                )

                buttonsBackground = arrayOf(
                    R.drawable.uikit_rectangle_12_e5e6eb,
                    R.drawable.uikit_primary_button_background_radius_12
                )

                callback = object : IDialogCallback() {
                    override fun clickCenter() {
                        super.clickCenter()
                        portrait?.setImageDrawable(null)
                        deletePortrait?.visibility = View.GONE
                        identityCardCallback?.deletePortrait()
                    }
                }
            }
        }

        // 国徽页
        nationalEmblem = binding.imgNationalEmblem
        deleteNationalEmblem = binding.deleteNationalEmblem
        binding.nationalEmblem.doOnClick {
            pictureIndex = NATIONAL_EMBLEM

            SelectPictureDialog.show {
                fm = fragmentManager
                callback = pictureCallback
            }
        }

        binding.deleteNationalEmblem.doOnClick {
            IToysNoticeDialog.show {
                this.title = context.getString(R.string.uikit_id_card_delete_national_emblem)
                fm = fragmentManager

                buttons = arrayOf(
                    context.getString(R.string.uikit_cancel),
                    context.getString(R.string.uikit_confirm),
                )

                buttonsBackground = arrayOf(
                    R.drawable.uikit_rectangle_12_e5e6eb,
                    R.drawable.uikit_primary_button_background_radius_12
                )

                callback = object : IDialogCallback() {
                    override fun clickCenter() {
                        super.clickCenter()
                        nationalEmblem?.setImageDrawable(null)
                        deleteNationalEmblem?.visibility = View.GONE
                        identityCardCallback?.deleteNationalEmblem()
                    }
                }
            }
        }
    }

    /**
     * 设置 fm
     */
    fun setFragmentManager(fm: FragmentManager) {
        this.fragmentManager = fm
    }

    /**
     * 设置图片选择回调
     */
    fun setPictureCallback(callback: SelectPictureDialog.ISelectCallback) {
        this.pictureCallback = callback
    }

    /**
     * 设置身份证选择回调
     */
    fun setIdentityCardCallback(callback: IIdentityCardCallback) {
        this.identityCardCallback = callback
    }

    /**
     * 添加单张图片
     */
    fun addPictures(picture: String, index: Int = pictureIndex) {
        when (index) {
            PORTRAIT -> {
                portraitPicture = picture
                setPicture(portrait, deletePortrait, picture)
            }

            NATIONAL_EMBLEM -> {
                nationalEmblemPicture = picture
                setPicture(nationalEmblem, deleteNationalEmblem, picture)
            }
        }
    }

    /**
     * 设置图片
     */
    private fun setPicture(
        imageView: AppCompatImageView?,
        deleteImageView: AppCompatImageView?,
        picture: String
    ) {
        if (picture.isBlank()) {
            imageView?.setImageDrawable(null)
            deleteImageView?.visibility = View.GONE
        } else {
            imageView?.loadRoundCornerImage(
                url = picture,
                radius = 2.dp2px(),
            )
            deleteImageView?.visibility = View.VISIBLE
        }
    }

    /**
     * 设置人像页图片
     */
    fun setPortraitPicture(portraitPicture: String) {
        this.portraitPicture = portraitPicture
        setPicture(portrait, deletePortrait, portraitPicture)
    }

    /**
     * 人像页图片
     */
    fun portraitPicture() = portraitPicture

    /**
     * 设置国徽页图片
     */
    fun setNationalEmblemPicture(nationalEmblemPicture: String) {
        this.nationalEmblemPicture = nationalEmblemPicture
        setPicture(nationalEmblem, deleteNationalEmblem, nationalEmblemPicture)
    }

    /**
     * 国徽页图片
     */
    fun nationalEmblemPicture() = nationalEmblemPicture

    interface IIdentityCardCallback {

        /**
         * 删除人像页图片
         */
        fun deletePortrait()

        /**
         * 删除国徽页图片
         */
        fun deleteNationalEmblem()
    }
}