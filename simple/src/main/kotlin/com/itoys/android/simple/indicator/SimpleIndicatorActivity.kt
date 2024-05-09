package com.itoys.android.simple.indicator

import android.os.Bundle
import com.itoys.android.core.activity.AbsActivity
import com.itoys.android.databinding.SimpleActivityIndicatorBinding
import com.itoys.android.uikit.adapter.PagerAdapter
import com.itoys.android.uikit.magicIndicator

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/19
 */
class SimpleIndicatorActivity : AbsActivity<SimpleActivityIndicatorBinding>() {

    override fun createViewBinding() = SimpleActivityIndicatorBinding.inflate(layoutInflater)

    override fun initialize(savedInstanceState: Bundle?) {
        initializeMagicIndicator()

        // 禁止滑动
        binding?.navHostFragment?.isUserInputEnabled = false
        val adapter = PagerAdapter(this)
        adapter.addFragment(IndicatorFragment.newInstance("TAB1"))
        adapter.addFragment(IndicatorFragment.newInstance("TAB2"))
        adapter.addFragment(IndicatorFragment.newInstance("TAB3"))
        binding?.navHostFragment?.adapter = adapter
    }

    override fun initData() {
    }

    /**
     * 初始化MagicIndicator
     */
    private fun initializeMagicIndicator() {
        binding?.magicIndicator?.magicIndicator(
            listOf("TAB1", "TAB2", "TAB3"),
            binding?.navHostFragment,
        )
    }
}