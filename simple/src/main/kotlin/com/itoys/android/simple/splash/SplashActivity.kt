package com.itoys.android.simple.splash

import android.os.Bundle
import com.itoys.android.core.activity.AbsMviActivity
import com.itoys.android.core.mvi.IUIState
import com.itoys.android.housing.databinding.SplashActivityLayoutBinding
import com.itoys.android.splash.SplashState
import com.itoys.android.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/3/24
 */
@AndroidEntryPoint
class SplashActivity : AbsMviActivity<SplashActivityLayoutBinding, SplashViewModel>() {

    override val viewModel: SplashViewModel? = null

    override fun createViewBinding() = SplashActivityLayoutBinding.inflate(layoutInflater)

    override fun initialize(savedInstanceState: Bundle?) {
    }
}