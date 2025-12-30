package com.itoys.android.image

import android.util.Log
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank
import com.luck.picture.lib.entity.LocalMedia

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/20
 */
data class ImageMedia(val mediaPath: String) {

    companion object {
        fun copy(media: LocalMedia): ImageMedia {
            Log.i(
                "ImageMedia",
                "copy: compressed -> ${media.isCompressed}, size -> ${media.size}, isOriginal -> ${media.isOriginal}, realPath -> ${media.realPath}, compressPath -> ${media.compressPath}, path -> ${media.path}"
            )

            var path = media.compressPath.invalid()
            if (path.isBlank()) {
                path = media.realPath.invalid()
            }

            return ImageMedia(path)
        }
    }
}
