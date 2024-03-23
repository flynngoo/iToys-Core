package com.itoys.android.uikit.model

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/18
 */
data class StepsModel(
    val title: String,
    val subtitle: String,
) {
    /** 当前状态 */
    var status: StepsStatus = StepsStatus.NotStarted

    /** 间距 */
    var margin: Int = 0
}

enum class StepsStatus {

    /** 未开始 */
    NotStarted,

    /** 进行中 */
    Progress,

    /** 已完成 */
    Completed,
}
