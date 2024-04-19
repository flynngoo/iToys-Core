package com.itoys.android.simple.indicator

import android.os.Bundle
import com.itoys.android.core.fragment.AbsFragment
import com.itoys.android.databinding.SimpleActivityIndicatorBinding
import com.itoys.android.databinding.SimpleFragmentIndicatorBinding
import com.itoys.android.utils.expansion.put
import com.itoys.android.utils.expansion.stringExtra

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/19
 */
class IndicatorFragment : AbsFragment<SimpleFragmentIndicatorBinding>() {

    companion object {
        fun newInstance(title: String): IndicatorFragment {
            val args = Bundle()
            args.put("title" to title)
            val fragment = IndicatorFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createViewBinding() = SimpleFragmentIndicatorBinding.inflate(layoutInflater)

    override fun initialize(savedInstanceState: Bundle?) {
        binding?.indicator?.text = stringExtra("title")
    }

    override fun addClickListen() {
    }

    override fun initData() {
    }
}