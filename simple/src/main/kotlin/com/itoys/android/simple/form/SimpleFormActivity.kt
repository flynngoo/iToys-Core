package com.itoys.android.simple.form

import android.os.Bundle
import androidx.activity.viewModels
import com.itoys.android.core.activity.AbsMviActivity
import com.itoys.android.databinding.SimpleActivityFormBinding
import com.itoys.android.uikit.components.form.IFormResultCallback
import com.itoys.android.utils.expansion.safeParseDouble
import com.itoys.android.utils.expansion.stripTrailingZeros
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/5/15
 */
@AndroidEntryPoint
class SimpleFormActivity : AbsMviActivity<SimpleActivityFormBinding, SimpleFormViewModel>() {

    /**
     * 总运费
     */
    private var shippingCost = 0.0

    /**
     * 现金运费
     */
    private var cashShippingCost = 0.0

    /**
     * 万金油油费
     */
    private var gasCost = 0.0

    override val viewModel: SimpleFormViewModel? by viewModels()

    override fun createViewBinding() = SimpleActivityFormBinding.inflate(layoutInflater)

    override fun initialize(savedInstanceState: Bundle?) {
    }

    override fun addClickListen() {
        super.addClickListen()
        binding?.cost?.setResultCallback(object : IFormResultCallback() {
            override fun result(result: String) {
                super.result(result)
                shippingCost = result.safeParseDouble()

                calculateCashCost()
                calculateGasCost()
            }
        })

        binding?.cashCost?.setResultCallback(object : IFormResultCallback() {
            override fun result(result: String) {
                super.result(result)
                cashShippingCost = result.safeParseDouble()
                calculateGasCost()
            }
        })

        binding?.gasCost?.setResultCallback(object : IFormResultCallback() {
            override fun result(result: String) {
                super.result(result)
                gasCost = result.safeParseDouble()
                calculateCashCost()
            }
        })
    }

    /**
     * 计算现金运费
     */
    private fun calculateCashCost() {
        cashShippingCost = shippingCost.minus(gasCost)
        if (cashShippingCost < 0) cashShippingCost = 0.0
        binding?.cashCost?.setContent(cashShippingCost.stripTrailingZeros())
    }

    /**
     * 计算万金油运费
     */
    private fun calculateGasCost() {
        gasCost = shippingCost.minus(cashShippingCost)
        if (gasCost < 0) gasCost = 0.0
        binding?.gasCost?.setContent(gasCost.stripTrailingZeros())
    }
}