package com.itoys.android.uikit.components.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/2
 */
abstract class IToysDialog<VB : ViewBinding, B : AbsDialog.Builder> : AbsDialog<B>() {

    /** view binding */
    protected open var binding: VB? = null

    /**
     * 创建ViewBinding.
     */
    abstract fun createViewBinding(inflater: LayoutInflater): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = createViewBinding(inflater)
        initializeDialog()
        return binding?.root
    }
}