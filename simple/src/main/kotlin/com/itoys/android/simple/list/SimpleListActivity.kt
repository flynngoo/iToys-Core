package com.itoys.android.simple.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import com.itoys.android.core.activity.AbsMviListActivity
import com.itoys.android.core.mvi.ListUIIntent
import com.itoys.android.databinding.SimpleLayoutFotterButtonBinding
import com.itoys.android.databinding.SimpleLayoutHeaderTotalCountBinding
import com.itoys.android.databinding.SimpleLayoutSearchBinding
import com.itoys.android.uikit.bindSteps
import com.itoys.android.uikit.components.input.SearchView
import com.itoys.android.uikit.magicIndicator
import com.itoys.android.uikit.model.StepsModel
import com.itoys.android.uikit.model.StepsStatus
import com.itoys.android.utils.expansion.dp2px
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/6
 */
@AndroidEntryPoint
class SimpleListActivity : AbsMviListActivity<SimpleListViewModel>() {

    override val viewModel: SimpleListViewModel? by viewModels()

    override fun listTitle() = "Simple List"

    override fun setupList() {
    }

    override fun headerView(parent: ViewGroup): View {
        val headerView = SimpleLayoutHeaderTotalCountBinding.inflate(
            LayoutInflater.from(this@SimpleListActivity),
            parent,
            false
        )

        headerView.totalCount.text = "共100条"

        val currentSteps = 0

        val list = listOf(
            StepsModel(title = "房源信息", subtitle = "填写房屋信息"),
            StepsModel(title = "填写信息", subtitle = "填写联系人信息"),
            StepsModel(title = "完成录入", subtitle = "创建完成"),
        )

        list.forEachIndexed { index, stepsModel ->
            stepsModel.margin = 52.dp2px()

            stepsModel.status = when {
                index < currentSteps ->  StepsStatus.Completed
                index == currentSteps ->  StepsStatus.Progress
                else -> StepsStatus.NotStarted
            }
        }

        headerView.steps.apply {
            self?.let {
                bindSteps(it, list)
            }
        }

        headerView.indicator.magicIndicator(listOf("TAB1", "TAB2", "TAB3"))
        return headerView.root
    }

    override fun footerView(parent: ViewGroup): View {
        val footerView = SimpleLayoutFotterButtonBinding.inflate(
            LayoutInflater.from(this@SimpleListActivity),
            parent,
            false
        )

        footerView.button.text = "确认"
        return footerView.root
    }
}