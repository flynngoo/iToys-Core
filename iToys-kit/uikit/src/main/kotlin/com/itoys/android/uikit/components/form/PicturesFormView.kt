package com.itoys.android.uikit.components.form

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.models
import com.drake.brv.utils.mutable
import com.google.android.material.textview.MaterialTextView
import com.itoys.android.image.IMediaCallback
import com.itoys.android.image.IPictureClickCallback
import com.itoys.android.image.ImageMedia
import com.itoys.android.image.addPictures
import com.itoys.android.image.pictureList
import com.itoys.android.image.selectFromAlbum
import com.itoys.android.image.takePicture
import com.itoys.android.image.toStringList
import com.itoys.android.image.uikit.dialog.ChooseImageDialog
import com.itoys.android.uikit.R
import com.itoys.android.uikit.components.dialog.IDialogCallback
import com.itoys.android.uikit.components.dialog.IToysNoticeDialog
import com.itoys.android.uikit.databinding.UikitLayoutFormPictureBinding
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.isNotBlank
import com.itoys.android.utils.expansion.then

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/19
 */
class PicturesFormView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /** 标题 */
    private var title = ""

    /** 标题字体大小 */
    private var titleSize = -1

    /** 标题 typeface */
    private var titleTypeface = ""

    /** 标题背景 */
    private var titleBackground = 0

    /** 标题 view */
    private var titleView: MaterialTextView? = null

    /** 图片list view */
    private var pictureListView: RecyclerView? = null

    /** 最大长度 */
    private var maximum = Int.MAX_VALUE

    /** 每行 count */
    private var spanCount = 4

    /** 展示添加 item */
    private var showPlus = true

    /** 展示删除 icon */
    private var showDelete = true

    /** owner */
    private var ownerActivity: AppCompatActivity? = null

    /** owner */
    private var ownerFragment: Fragment? = null

    /** 选择图片 */
    private val medias = arrayListOf<ImageMedia>()

    /** 图片选择回调 */
    private val mediaCallback by lazy {
        object : IMediaCallback() {
            override fun onResult(result: ImageMedia) {
                super.onResult(result)

                medias.add(result)
                addPictures(result.mediaPath)
            }

            override fun onResult(result: List<ImageMedia>?) {
                super.onResult(result)

                if (!result.isNullOrEmpty()) {
                    medias.addAll(result)
                    addPictures(result.toStringList())
                }
            }
        }
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        initView(attrs)
    }

    /**
     * 初始化
     */
    private fun initView(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PicturesFormView)
        title = ta.getString(R.styleable.PicturesFormView_pictureTitle).invalid()
        titleSize = ta.getDimensionPixelSize(R.styleable.PicturesFormView_pictureTitleSize, titleSize)
        titleTypeface = ta.getString(R.styleable.PicturesFormView_pictureTitleTypeface).invalid()
        titleBackground = ta.getColor(R.styleable.PicturesFormView_pictureTitleBackground, titleBackground)
        maximum = ta.getInt(R.styleable.PicturesFormView_pictureMaximum, maximum)
        spanCount = ta.getInt(R.styleable.PicturesFormView_pictureSpanCount, spanCount)
        showPlus = ta.getBoolean(R.styleable.PicturesFormView_picturePlus, showPlus)
        showDelete = ta.getBoolean(R.styleable.PicturesFormView_picturePlus, showDelete)
        ta.recycle()

        val binding = UikitLayoutFormPictureBinding.inflate(
            LayoutInflater.from(context), this, false
        )
        this.addView(binding.root)

        titleView = binding.title
        // 设置标题
        if (title.isNotBlank()) {
            titleView?.text = title
        }
        // 设置标题size
        if (titleSize != -1) {
            titleView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())
        }
        // 设置标题颜色
        if (titleBackground != 0) {
            titleView?.setBackgroundColor(titleBackground)
        }
        // 设置标题typeface
        if (titleTypeface.isNotBlank()) {
            titleView?.typeface = Typeface.create(titleTypeface, Typeface.NORMAL)
        }

        pictureListView = binding.list
        pictureListView?.pictureList(
            spanCount = spanCount,
            withPlus = showPlus,
            withDelete = showDelete,
            pictures = showPlus.then(listOf(""), listOf()),
            clickCallback = object : IPictureClickCallback {
                override fun selectPicture() {
                    ChooseImageDialog.show {
                        fm = fragmentManager()

                        callback = object : ChooseImageDialog.ISelectCallback {
                            override fun selectFromAlbum() {
                                val maximum = (maximum < 9).then(maximum, 9)
                                ownerActivity?.selectFromAlbum(callback = mediaCallback, maxSize = maximum)
                                ownerFragment?.selectFromAlbum(callback = mediaCallback, maxSize = maximum)
                            }

                            override fun takePicture() {
                                ownerActivity?.takePicture(callback = mediaCallback)
                                ownerFragment?.takePicture(callback = mediaCallback)
                            }
                        }
                    }
                }

                override fun delete(index: Int) {
                    IToysNoticeDialog.show {
                        fm = fragmentManager()
                        title = context.getString(R.string.image_delete_this_picture)

                        callback = object : IDialogCallback() {

                            override fun clickCenter() {
                                super.clickCenter()
                                deletePicture(index)
                            }
                        }
                    }
                }
            }
        )
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String) {
        titleView?.text = title
    }

    /**
     * 添加单张图片
     */
    fun addPictures(picture: String) {
        if (picture.isBlank()) return

        pictureListView?.addPictures(
            maximum = maximum,
            withPlus = showPlus,
            pictures = listOf(picture),
        )
    }

    /**
     * 添加多张图片
     */
    fun addPictures(pictures: List<String>?) {
        if (pictures.isNullOrEmpty()) return

        pictureListView?.addPictures(
            maximum = maximum,
            withPlus = showPlus,
            pictures = pictures,
        )
    }

    /**
     * 添加多张图片
     */
    fun addMediaResult(result: List<ImageMedia>?) {
        if (result.isNullOrEmpty()) return

        pictureListView?.addPictures(
            maximum = maximum,
            withPlus = showPlus,
            pictures = result.toStringList(),
        )
    }

    /**
     * 删除图片
     */
    fun deletePicture(index: Int) {
        pictureListView?.mutable?.removeAt(index)
        pictureListView?.bindingAdapter?.notifyItemRemoved(index)
        // 删除照片
        medias.removeAt(index)

        // 如果删除后没有图片 && 支持选择上传
        // 兼容 maximum = 1 的情况
        if (pictureListView?.models?.size == 0 && showPlus) {
            pictureListView?.models = listOf("")
        }
    }

    /**
     * 设置owner
     */
    fun setOwner(owner: AppCompatActivity) {
        ownerActivity = owner
    }

    /**
     * 设置owner
     */
    fun setOwner(owner: Fragment) {
        ownerFragment = owner
    }

    /**
     * 获取fragmentManager
     */
    private fun fragmentManager(): FragmentManager? {
        return ownerFragment?.childFragmentManager ?: ownerActivity?.supportFragmentManager
    }

    /**
     * 获取图片list
     */
    fun mediaList() = medias

    /**
     * 获取图片路径list
     */
    fun mediaPathList() = medias.toStringList()
}