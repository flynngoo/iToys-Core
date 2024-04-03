package com.itoys.android.core.router

import android.os.Bundle
import androidx.activity.viewModels
import com.itoys.android.core.activity.AbsMviActivity
import com.itoys.android.core.databinding.CoreActivityPageNotFoundBinding
import com.itoys.android.utils.expansion.doOnClick
import com.therouter.router.Route

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/4/3
 */
@Route(path = Address.PAGE_NOT_FOUND, description = "页面不存在")
class PageNotFoundActivity : AbsMviActivity<CoreActivityPageNotFoundBinding, PageNotFoundViewModel>() {

    override val viewModel: PageNotFoundViewModel? by viewModels()

    override fun createViewBinding() = CoreActivityPageNotFoundBinding.inflate(layoutInflater)

    override fun initialize(savedInstanceState: Bundle?) {
        binding?.btnRetry?.doOnClick {
            viewModel?.sendIntent(PageNotFoundIntent.Navigation)
            finish()
        }
    }
}