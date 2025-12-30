package com.itoys.android.uikit.components.upload

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.itoys.android.image.IMediaCallback
import com.itoys.android.image.ImageMedia
import com.itoys.android.image.selectFromAlbum
import com.itoys.android.image.takePicture
import com.itoys.android.image.toStringList
import com.itoys.android.image.uikit.dialog.ChooseImageDialog
import com.itoys.android.uikit.R
import com.itoys.android.uikit.components.image.IViewImageCallback
import com.itoys.android.uikit.components.toast.toast
import com.itoys.android.uikit.viewImage
import com.itoys.android.utils.FileUtils
import com.itoys.android.utils.SysUtils
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isNotNull
import com.itoys.android.utils.expansion.isNull
import com.itoys.android.utils.expansion.launchOnIO
import kotlin.math.max

/**
 * @Author Flynn Gu
 * @Email gufanfan.worker@gmail.com
 * @Date 2025/11/20
 */
class UploadHelper(
    private val activity: AppCompatActivity,
    private val uploadCallback: OnUploadCallback
) {

    private var currentUploadMark = ""

    /** 图片选择回调 */
    private val mediaCallback by lazy {
        object : IMediaCallback() {
            override fun onResult(result: ImageMedia) {
                super.onResult(result)

                uploadCallback.onFileSelected(currentUploadMark, result.mediaPath.toImageItem())
            }

            override fun onResult(result: List<ImageMedia>?) {
                super.onResult(result)
                uploadCallback.onFileSelected(
                    currentUploadMark,
                    result?.toStringList()?.toImageItemList() ?: emptyList()
                )
            }
        }
    }

    /** 查看大图回调 */
    private val viewImageCallback by lazy {
        object : IViewImageCallback {
            override fun onDelete(position: Int) {
                uploadCallback.onFileDeselected(currentUploadMark, position)
            }
        }
    }

    /**
     * 选择文件回调
     */
    private val pickFileLauncher =
        activity.registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri.isNotNull()) {
                launchOnIO {
                    val file = FileUtils.uriToFile(activity, uri = uri!!)

                    val fileItem = uri.toString().toFileItem()
                    fileItem.file = file
                    uploadCallback.onFileSelected(currentUploadMark, fileItem)
                }
            } else {
                uploadCallback.onSelectedFailed(currentUploadMark, "文件未找到, 请重新选择")
            }
        }

    /** 选择图片 */
    fun choose(mark: String, uploadType: UploadType, chooseSize: Int = 1) {
        when (uploadType) {
            UploadType.IMAGE -> chooseImage(mark, chooseSize)

            UploadType.DOCUMENT -> chooseFile(mark)
        }
    }

    /** 选择图片 */
    fun chooseImage(mark: String, chooseSize: Int) {
        currentUploadMark = mark

        ChooseImageDialog.show {
            fm = activity.supportFragmentManager

            callback = object : ChooseImageDialog.ISelectCallback {
                override fun selectFromAlbum() {
                    activity.selectFromAlbum(maxSize = chooseSize, callback = mediaCallback)
                }

                override fun takePicture() {
                    activity.takePicture(callback = mediaCallback)
                }
            }
        }
    }

    /** 选择文件（图片 或 任意文件） */
    fun chooseFile(mark: String, mimeType: String = "application/*") {
        currentUploadMark = mark
        pickFileLauncher.launch(arrayOf(mimeType))
    }

    /** 预览 */
    fun preview(
        mark: String,
        item: UploadItem?,
    ) {
        if (item.isNull()) return

        currentUploadMark = mark
        if (item?.type == UploadType.IMAGE) {
            activity.viewImage(
                item.itemUrl(),
                showDownload = !item.isCanDelete,
                showDelete = item.isCanDelete,
                callback = viewImageCallback
            )
        } else {
            SysUtils.openSysBrowser(activity, item?.url.invalid())
        }
    }

    /** 预览 */
    fun preview(
        mark: String,
        items: List<UploadItem>,
        position: Int,
        showDownload: Boolean = false,
        showDelete: Boolean = false,
    ) {
        if (items.isNull()) return

        currentUploadMark = mark
        activity.viewImage(
            items.toImageUrlList(),
            position,
            showDownload = showDownload,
            showDelete = showDelete,
            callback = viewImageCallback
        )
    }
}