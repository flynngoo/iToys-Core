package com.itoys.android.location

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2024/3/1
 */
data class LocationModel(
    val address: String = "", // 详细地址
    val latitude: Double = 0.0, // 纬度
    val longitude: Double = 0.0, // 经度
    val province: String = "", // 省
    val city: String = "", // 市
    val district: String = "", // 区
    val street: String = "", // 街道
    val streetNumber: String = "", // 门牌号
    val adCode: String = "", // 行政区划编码
)
