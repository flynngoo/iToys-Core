package com.itoys.android.image

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
            var path = media.compressPath.invalid()
            if (path.isBlank()) {
                path = media.realPath.invalid()
            }

            return ImageMedia(path)
        }
    }
}
