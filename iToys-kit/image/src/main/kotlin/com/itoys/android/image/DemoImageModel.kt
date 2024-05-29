package com.itoys.android.image

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/28
 */
data class DemoImageModel(
    val imageTitle: String = "",
    val image: Int = -1,
    val imageUrl: String = "",
    val imageText: String = "请按照示例上传\n模糊、遮挡会导致证件无法识别，认证不通过",
)