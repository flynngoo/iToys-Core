package com.itoys.android.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/11/30
 */
abstract class AbsFragment<VB : ViewBinding> : Fragment() {

    /** view binding */
    protected open var binding: VB? = null

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
}