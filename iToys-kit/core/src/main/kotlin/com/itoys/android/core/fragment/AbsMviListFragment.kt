package com.itoys.android.core.fragment

import android.os.Bundle
import android.view.View
import com.itoys.android.core.databinding.CoreLayoutListBinding
import com.itoys.android.core.mvi.AbsListViewModel
import com.itoys.android.core.mvi.IUIIntent
import com.itoys.android.core.mvi.IUIState
import com.itoys.android.core.mvi.ListUIIntent
import com.itoys.android.core.mvi.ListUIState
import com.itoys.android.utils.expansion.collect
import com.itoys.android.utils.expansion.then

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/11
 */
abstract class AbsMviListFragment<VM : AbsListViewModel<out IUIIntent, out IUIState>> : AbsMviFragment<CoreLayoutListBinding, VM>()  {

    /**
     * 是否显示标题栏
     */
    open val withTileBar = false

    /**
     * 是否显示搜索组件
     */
    open val withSearchView = false

    /**
     * 开启刷新
     */
    open val enableRefresh = true

    /**
     * 开启加载更多
     */
    open val enableLoadMore = true

    override fun createViewBinding() = CoreLayoutListBinding.inflate(layoutInflater)

    override fun initialize(savedInstanceState: Bundle?) {
        binding?.titleBar?.setTitle(listTitle())
        binding?.titleBar?.visibility = withTileBar.then(View.VISIBLE, View.GONE)
        binding?.searchLayout?.visibility = withSearchView.then(View.VISIBLE, View.GONE)
        initSearchView()

        setupList()
    }

    /**
     * 初始化搜索组件
     */
    private fun initSearchView() {
        val searchView = searchView() ?: return
        binding?.searchLayout?.addView(searchView)
    }

    override fun addClickListen() {
        binding?.page?.apply {
            // 刷新
            setEnableRefresh(enableRefresh)
            if (enableRefresh) {
                onRefresh { viewModel?.sendListIntent(ListUIIntent.Refresh) }.refresh()
            }

            // 加载更多
            setEnableLoadMore(enableLoadMore)
            if (enableLoadMore) {
                onLoadMore { viewModel?.sendListIntent(ListUIIntent.LoadMore) }
            }
        }
    }

    /**
     * list title
     */
    open fun listTitle(): String = ""

    /**
     * 搜索组件
     */
    open fun searchView(): View? = null

    /**
     * 设置list
     */
    abstract fun setupList()

    override fun addObserver() {
        super.addObserver()

        viewModel?.apply { collect(listState, ::collectList) }
    }

    /**
     * collect list
     */
    protected open fun collectList(list: ListUIState?) {
        when (list) {
            is ListUIState.LoadingComplete -> {
                if (list.isRefresh) {
                    binding?.page?.showContent()
                } else {
                    binding?.page?.finishLoadMore(list.isSuccessful)
                }
            }

            ListUIState.NoMore -> {
                binding?.page?.finishLoadMoreWithNoMoreData()
            }

            ListUIState.ShowEmpty -> binding?.page?.showEmpty()

            else -> {}
        }
    }
}