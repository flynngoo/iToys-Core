package com.itoys.android.image

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.addModels
import com.drake.brv.utils.divider
import com.drake.brv.utils.grid
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.itoys.android.image.databinding.ImageLayoutItemPictureBinding
import com.itoys.android.image.engine.CompressImageEngine
import com.itoys.android.image.glide.GlideImageEngine
import com.itoys.android.image.glide.IToysGlide
import com.itoys.android.utils.expansion.dp2px
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.isNotBlank
import com.itoys.android.utils.expansion.then
import com.itoys.android.utils.expansion.trimString
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import jp.wasabeef.transformers.glide.BlurTransformation
import jp.wasabeef.transformers.glide.CropCenterTransformation
import jp.wasabeef.transformers.glide.CropCircleTransformation
import jp.wasabeef.transformers.glide.CropCircleWithBorderTransformation
import jp.wasabeef.transformers.glide.RoundedCornersTransformation
import kotlin.math.max

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/19
 */

@JvmOverloads
fun ImageView.loadImage(
    @RawRes @DrawableRes drawableId: Int,
    @RawRes @DrawableRes errorId: Int = drawableId,
) {
    IToysGlide.with(this).load(drawableId).placeholder(drawableId).error(errorId).into(this)
}

@JvmOverloads
fun ImageView.loadImage(
    url: String?,
    @RawRes @DrawableRes placeholder: Int = R.drawable.image_rectangle_placeholder,
    @RawRes @DrawableRes error: Int = placeholder,
) {
    IToysGlide.with(this).load(url).placeholder(placeholder).error(error).into(this)
}

@JvmOverloads
fun ImageView.loadResizeImage(
    url: String?,
    width: Int,
    height: Int,
    @RawRes @DrawableRes placeholder: Int = R.drawable.image_rectangle_placeholder,
    @RawRes @DrawableRes error: Int = placeholder,
) {
    val options = RequestOptions().placeholder(placeholder)
        .error(error)
        .override(width, height)

    IToysGlide.with(this).load(url).apply(options).into(this)
}

@JvmOverloads
fun ImageView.loadCircleImage(
    url: String?,
    @RawRes @DrawableRes placeholder: Int = R.drawable.image_rectangle_placeholder,
    @RawRes @DrawableRes error: Int = placeholder,
) {
    val options = RequestOptions().transform(
        CropCenterTransformation(),
        CropCircleTransformation()
    )

    IToysGlide.with(this)
        .load(url)
        .placeholder(placeholder)
        .error(error)
        .apply(options)
        .into(this)
}

@JvmOverloads
fun ImageView.loadCircleWithBorderImage(
    url: String?,
    @RawRes @DrawableRes placeholder: Int = R.drawable.image_rectangle_placeholder,
    @RawRes @DrawableRes error: Int = placeholder,
    borderSize: Int = 1,
    @ColorInt borderColor: Int = Color.WHITE,
) {
    val options = RequestOptions().transform(
        CropCenterTransformation(),
        CropCircleWithBorderTransformation(
            borderSize = borderSize,
            borderColor = borderColor
        )
    )

    IToysGlide.with(this)
        .load(url)
        .placeholder(placeholder)
        .error(error)
        .apply(options)
        .into(this)
}

@JvmOverloads
fun ImageView.loadRoundCornerImage(
    url: String?,
    radius: Int = 0,
    @RawRes @DrawableRes placeholder: Int = R.drawable.image_rectangle_placeholder,
    @RawRes @DrawableRes error: Int = placeholder,
) {
    val options = RequestOptions().transform(
        CenterCrop(),
        RoundedCornersTransformation(radius)
    )

    IToysGlide.with(this)
        .load(url.trimString())
        .placeholder(placeholder)
        .error(error)
        .apply(options)
        .into(this)
}

@JvmOverloads
fun ImageView.loadBlurImage(
    url: String?,
    radius: Int = 25,
    sampling: Int = 4,
    @RawRes @DrawableRes placeholder: Int = R.drawable.image_rectangle_placeholder,
    @RawRes @DrawableRes error: Int = placeholder,
) {
    val options = RequestOptions().transform(
        CropCenterTransformation(),
        BlurTransformation(this.context, radius, sampling)
    )

    IToysGlide.with(this)
        .load(url)
        .placeholder(placeholder)
        .error(error)
        .apply(options)
        .into(this)
}

/**
 * 图片列表
 *
 * [spanCount] 一行展示数量
 * [withPlus] 是否展示添加item
 * [withDelete] 如果图片存在是否显示delete按钮
 * [pictures] 图片资源
 */
fun RecyclerView.pictureList(
    spanCount: Int = 4,
    withPlus: Boolean = true,
    withDelete: Boolean = true,
    pictures: List<String>? = null,
    clickCallback: IPictureClickCallback? = null,
) {
    grid(spanCount = spanCount).divider {
        orientation = DividerOrientation.GRID
        setDivider(8, true)
    }.setup {
        addType<String>(R.layout.image_layout_item_picture)
        onBind {
            val itemBinding = getBinding<ImageLayoutItemPictureBinding>()
            val item: String? = getModel()
            if (withPlus && item.isBlank()) {
                // 如果item是空的 && 需要暂时添加按钮
                itemBinding.picture.setImageResource(R.drawable.image_icon_add_picture)
            } else {
                // 加载图片
                itemBinding.picture.loadRoundCornerImage(item, radius = 2.dp2px())
            }

            // 删除按钮显示
            itemBinding.delete.visibility = (item.isNotBlank() && withDelete).then(
                View.VISIBLE, View.GONE
            )
        }

        R.id.picture.onClick {
            val item: String? = getModel()
            if (withPlus && item.isBlank()) {
                // 图片选择
                clickCallback?.selectPicture()
            } else {
                // 查看大图
            }
        }

        R.id.delete.onClick {
            // 删除图片
            clickCallback?.delete(layoutPosition)
        }
    }.models = pictures
}

/**
 * 添加图片
 * [maximum] 最大数量限制
 * [withPlus] 最后是否展示添加item
 * [pictures] 添加图片 list
 */
fun RecyclerView.addPictures(
    maximum: Int = Int.MAX_VALUE,
    withPlus: Boolean = true,
    pictures: List<String>,
) {
    val size = this.models?.size ?: 0

    when {
        withPlus && size < maximum -> {
            addModels(pictures, index = max((size - 1), 0))
        }
        size == maximum -> {
            models = pictures
        }
        else -> addModels(pictures)
    }

    // 刷新分割线
    invalidateItemDecorations()
}

/**
 * 拍照
 */
@JvmOverloads
fun AppCompatActivity.takePicture(
    callback: IMediaCallback? = null
) {
    PictureSelector.create(this)
        .openCamera(SelectMimeType.ofImage())
        .setCompressEngine(CompressImageEngine.create())
        .forResult(object : OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: ArrayList<LocalMedia>?) {
                if (result.isNullOrEmpty()) {
                    callback?.onResult(null)
                    return
                }

                if (result.size == 1) {
                    callback?.onResult(ImageMedia.copy(result[0]))
                    return
                }


                callback?.onResult(result.toMedias())
            }

            override fun onCancel() {
                callback?.onCancel()
            }
        })
}

/**
 * 拍照
 */
@JvmOverloads
fun Fragment.takePicture(
    callback: IMediaCallback? = null
) {
    PictureSelector.create(this)
        .openCamera(SelectMimeType.ofImage())
        .setCompressEngine(CompressImageEngine.create())
        .forResult(object : OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: ArrayList<LocalMedia>?) {
                if (result.isNullOrEmpty()) {
                    callback?.onResult(null)
                    return
                }

                if (result.size == 1) {
                    callback?.onResult(ImageMedia.copy(result[0]))
                    return
                }

                callback?.onResult(result.toMedias())
            }

            override fun onCancel() {
                callback?.onCancel()
            }
        })
}

@JvmOverloads
fun AppCompatActivity.selectFromAlbum(
    maxSize: Int = 1,
    callback: IMediaCallback? = null
) {
    PictureSelector.create(this)
        .openGallery(SelectMimeType.ofImage())
        .isDisplayCamera(false)
        .setMaxSelectNum(maxSize)
        .setMinSelectNum(1)
        .setImageEngine(GlideImageEngine)
        .setCompressEngine(CompressImageEngine.create())
        .forResult(object : OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: ArrayList<LocalMedia>?) {
                if (result.isNullOrEmpty()) {
                    callback?.onResult(null)
                    return
                }

                if (result.size == 1) {
                    callback?.onResult(ImageMedia.copy(result[0]))
                    return
                }

                callback?.onResult(result.toMedias())
            }

            override fun onCancel() {
                callback?.onCancel()
            }
        })
}

@JvmOverloads
fun Fragment.selectFromAlbum(
    maxSize: Int = 1,
    callback: IMediaCallback? = null
) {
    PictureSelector.create(this)
        .openGallery(SelectMimeType.ofImage())
        .isDisplayCamera(false)
        .setMaxSelectNum(maxSize)
        .setMinSelectNum(1)
        .setImageEngine(GlideImageEngine)
        .setCompressEngine(CompressImageEngine.create())
        .forResult(object : OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: ArrayList<LocalMedia>?) {
                if (result.isNullOrEmpty()) {
                    callback?.onResult(null)
                    return
                }

                if (result.size == 1) {
                    callback?.onResult(ImageMedia.copy(result[0]))
                    return
                }

                callback?.onResult(result.toMedias())
            }

            override fun onCancel() {
                callback?.onCancel()
            }
        })
}

fun LocalMedia.toModel() = ImageMedia.copy(this)

fun Iterable<LocalMedia>.toMedias(): List<ImageMedia> = map(LocalMedia::toModel)

fun Iterable<ImageMedia>.toStringList(): List<String> = map(ImageMedia::mediaPath)

fun String.isLocalAddress() = this.startsWith("/")

fun String?.isRemoteAddress() = this.isNotBlank() && (this?.startsWith("http") == true)