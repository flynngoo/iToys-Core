package com.itoys.android.core.mvi

import com.itoys.android.core.network.PageEntity
import com.itoys.android.core.network.RequestAction
import com.itoys.android.core.network.ResultException
import com.itoys.android.logcat.logcat
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.isBlank
import com.itoys.android.utils.expansion.then
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/10/23
 */
abstract class AbsListViewModel<I : IUIIntent, S : IUIState> : AbsViewModel<I, S>() {

    /** view intent */
    private val _listIntent: Channel<ListUIIntent> = Channel()
    private val listIntentFlow: Flow<ListUIIntent> = _listIntent.receiveAsFlow()

    /** loading state */
    private val _listState: Channel<ListUIState> = Channel()
    val listState: Flow<ListUIState> = _listState.receiveAsFlow()

    /**
     * 页码
     */
    open var page = 1

    /**
     * 页码 - key
     */
    open var pageKey = "current"

    /**
     * 分页数量
     */
    open val pageSize = 20

    /**
     * 分页数量 - key
     */
    open val pageSizeKey = "size"

    /**
     * 搜索关键之 - key
     */
    open val searchKey = "keywords"

    /**
     * 分页参数
     */
    val pageParams = hashMapOf<String, Any>()

    init {
        launchOnUI {
            listIntentFlow.collect { intent -> handlerListIntent(intent) }
        }
    }

    /**
     * 处理list 事件
     */
    private fun handlerListIntent(intent: ListUIIntent) {
        when (intent) {
            ListUIIntent.Refresh -> refresh()
            ListUIIntent.LoadMore -> loadMore()
            is ListUIIntent.Search -> search(intent.keywords)
        }
    }

    /**
     * 发送list 事件
     */
    fun sendListIntent(intent: ListUIIntent) {
        launchOnUI { _listIntent.send(intent) }
    }

    /**
     * 发送 view 状态
     */
    open fun sendListState(block: ListUIState) {
        launchOnUI { _listState.send(block) }
    }

    /**
     * 刷新
     */
    open fun refresh() {
        logcat { "刷新数据" }

        page = 1
        pageParams[pageKey] = page
        pageParams[pageSizeKey] = pageSize

        fetchData(ListUIIntent.Refresh)
    }

    /**
     * 加载更多
     */
    open fun loadMore() {
        logcat { "加载更多" }

        page++
        pageParams[pageKey] = page
        pageParams[pageSizeKey] = pageSize

        fetchData(ListUIIntent.LoadMore)
    }

    /**
     * 搜索
     */
    open fun search(keywords: String) {
        pageParams[searchKey] = keywords

        if (keywords.isBlank()) {
            pageParams.remove(searchKey)
        }

        refresh()
    }

    /**
     * 获取数据
     */
    abstract fun fetchData(intent: ListUIIntent)

    /**
     * 解析pager
     */
    fun parsePager(pager: PageEntity<*>?, intent: ListUIIntent, isSuccessful: Boolean = true) {
        if (pager == null) {
            sendListState(
                ListUIState.LoadingComplete(
                    isRefresh = intent == ListUIIntent.Refresh,
                    isSuccessful = isSuccessful,
                )
            )

            // 显示缺省页
            if (intent == ListUIIntent.Refresh) sendListState(ListUIState.ShowEmpty)
            return
        }

        // 结束刷洗/加载更多
        sendListState(
            ListUIState.LoadingComplete(
                isRefresh = intent == ListUIIntent.Refresh,
                isSuccessful = isSuccessful,
            )
        )

        // 没有更多
        if (pager.current >= pager.pages) {
            sendListState(ListUIState.NoMore)
        }

        // 显示缺省页
        if (pager.list.isNullOrEmpty()) {
            sendListState(ListUIState.ShowEmpty)
        }
    }

    /**
     * 是否是第一页
     */
    fun isFirstPage() = page == 1

    /**
     * 封装请求action
     */
    fun <T> RequestAction<T>.handlerPagerAction(
        success: (T?) -> Unit,
        failure: (ResultException) -> Unit = ::handlePagerFailure
    ) {
        start { sendLoading(LoadingUIState.Loading(it)) }
        success(success)
        failure(failure)
        finish { sendLoading(LoadingUIState.Loading(showLoading = false)) }
    }

    open fun handlePagerFailure(failure: ResultException) {
        val intent = isFirstPage().then(ListUIIntent.Refresh, ListUIIntent.LoadMore)
        parsePager(null, intent, isSuccessful = false)
        val msg = failure.msg.invalid("请求出现异常")
        sendToast(ToastUIState.Toast(msg))
    }
}