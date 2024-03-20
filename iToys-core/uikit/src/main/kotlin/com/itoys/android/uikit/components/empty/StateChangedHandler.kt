package com.itoys.android.uikit.components.empty

import android.view.View

/**
 * @author Fanfan.gu <a href="mailto:fanfan.work@outlook.com">Contact me.</a>
 * @date 15/04/2023
 * @desc 缺省页切换处理
 */
interface StateChangedHandler {

    companion object DEFAULT : StateChangedHandler

    /**
     * StateLayout添加缺省页
     * @param container StateLayout
     * @param stateView 将被添加缺省页视图对象
     * @param tag 显示状态传入的tag
     */
    fun onAdd(container: EmptyLayout, stateView: View, status: EmptyStatus, tag: String?) {
        if (container.currStatus == status) return

        if (container.indexOfChild(stateView) != -1) {
            stateView.visibility = View.VISIBLE
        } else {
            container.addView(stateView)
        }
    }

    /**
     * StateLayout删除缺省页, 此方法比[onAdd]先执行
     * @param container StateLayout
     * @param stateView 将被删除缺省页视图对象
     * @param tag 显示状态传入的tag
     */
    fun onRemove(container: EmptyLayout, stateView: View, status: EmptyStatus, tag: String?) {
        if (container.currStatus != status) {
            stateView.visibility = View.GONE
        }
    }
}