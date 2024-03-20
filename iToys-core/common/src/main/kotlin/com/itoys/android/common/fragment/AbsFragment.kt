package com.itoys.android.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.itoys.android.logcat.logcat
import com.itoys.android.uikit.components.empty.EmptyLayout
import com.itoys.android.uikit.components.empty.addStateLayout
import com.itoys.android.utils.expansion.then

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/30
 */
abstract class AbsFragment<VB : ViewBinding> : Fragment() {

    /** view binding */
    protected open var binding: VB? = null

    /** Empty 空状态 */
    protected open var emptyLayout: EmptyLayout? = null

    /**
     * Empty - 重试
     */
    protected open val emptyLayoutRetry: () -> Unit = {
        logcat { ">>>>>>>>>> Empty - 重试 <<<<<<<<<<" }
    }

    /** 是否已经加载过 */
    private var isLoaded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = createViewBinding()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize(savedInstanceState)
        loadEmptyLayout()
        initData()
        addClickListen()
    }

    /**
     * 创建ViewBinding.
     */
    abstract fun createViewBinding(): VB

    /**
     * 初始化
     */
    abstract fun initialize(savedInstanceState: Bundle?)

    /**
     * 添加点击事件
     */
    abstract fun addClickListen()

    /**
     * 懒加载初始化
     */
    abstract fun initData()

    /**
     * 使用 empty layout
     */
    protected open fun useEmptyLayout() = false

    /**
     * 加载 state layout.
     */
    protected open fun loadEmptyLayout(view: View? = null) {
        if (useEmptyLayout()) {
            emptyLayout = (view == null).then({
                addStateLayout(retry = emptyLayoutRetry)
            }, {
                view?.addStateLayout(retry = emptyLayoutRetry)
            })
        }
    }
}