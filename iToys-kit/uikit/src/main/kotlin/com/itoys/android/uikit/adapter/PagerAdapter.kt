package com.itoys.android.uikit.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/18
 */
class PagerAdapter : FragmentStateAdapter {

    constructor(activity: FragmentActivity) : super(activity)

    constructor(fragment: Fragment) : super(fragment)

    private val fragments = ArrayList<Fragment>()

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]

    /**
     * 添加fragment
     */
    fun addFragment(fragment: Fragment?) {
        fragment?.let { fragments.add(fragment) }
    }

    /**
     * fragment list
     */
    fun fragmentList() = fragments
}