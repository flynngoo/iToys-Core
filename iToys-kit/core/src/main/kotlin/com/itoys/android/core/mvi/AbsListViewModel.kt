package com.itoys.android.core.mvi

import com.itoys.android.core.CoreConfig
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

    private companion object {
        /**
         * 验证码倒计间隔
         * 单位：ms
         */
        const val COUNT_DOWN_TIMER_INTERVAL = 1000L
    }

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
     * 分页数量
     */
    open val pageSize = 20

    /**
     * 分页参数
     */
    val pageParams = hashMapOf<String, Any>()

    /**
     * 搜索倒计时
     */
    private var searchCountDownTimer: SearchCountDownTimer? = null

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
        pageParams[CoreConfig.pageKey] = page
        pageParams[CoreConfig.pageSizeKey] = pageSize

        fetchData(ListUIIntent.Refresh)
    }

    /**
     * 加载更多
     */
    open fun loadMore() {
        logcat { "加载更多" }

        page++
        pageParams[CoreConfig.pageKey] = page
        pageParams[CoreConfig.pageSizeKey] = pageSize

        fetchData(ListUIIntent.LoadMore)
    }

    /**
     * 搜索
     */
    open fun search(keywords: String) {
        pageParams[CoreConfig.searchKey] = keywords
        startSearchCountDownTimer(keywords)

        if (keywords.isBlank()) {
            // 关键字是空的则刷新
            pageParams.remove(CoreConfig.searchKey)
            refresh()
        }
    }

    /**
     * 启动新的搜索倒计时
     */
    private fun startSearchCountDownTimer(keywords: String) {
        searchCountDownTimer?.cancel()
        searchCountDownTimer = null
        if (keywords.isBlank()) return

        searchCountDownTimer = SearchCountDownTimer(
            viewModel = this@AbsListViewModel,
            searchKeywords = keywords,
            CoreConfig.searchCountdownTimerFuture,
            COUNT_DOWN_TIMER_INTERVAL
        )
        searchCountDownTimer?.start()
    }

    /**
     * 倒计时结束
     */
    fun countdownEnded(keywords: String) {
        if (pageParams[CoreConfig.searchKey] == keywords) {
            // 倒计时的关键字和当前关键字一致则刷新
            logcat { "当前搜索关键字: $keywords" }
            refresh()
        }
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
        // 1. 当前页码是最后一页( >= 总页数)
        // 2. 当前总条数(当前页码 * 每页数量) >= 总条数
        val isNoMore = pager.current >= pager.pages || pager.current * pager.pageNum >= pager.total
        if (isNoMore) {
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