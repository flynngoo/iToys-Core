package com.itoys.android.common.mvi

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/23
 */
/**
 * view 事件
 */
interface IUIIntent

/**
 * list 事件
 */
sealed class ListUIIntent : IUIIntent {

    /**
     * 刷新事件
     */
    data object Refresh : ListUIIntent()

    /**
     * 加载更多事件
     */
    data object LoadMore : ListUIIntent()

    /**
     * 搜索
     */
    data class Search(val keywords: String) : ListUIIntent()
}