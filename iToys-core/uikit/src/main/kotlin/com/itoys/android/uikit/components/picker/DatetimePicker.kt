package com.itoys.android.uikit.components.picker

import android.view.Gravity
import android.view.LayoutInflater
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.itoys.android.uikit.R
import com.itoys.android.uikit.components.dialog.AbsDialog
import com.itoys.android.uikit.components.dialog.IToysDialog
import com.itoys.android.uikit.databinding.UikitLayoutPickerDatetimeBinding
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.isNotBlank

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/26
 */
class DatetimePicker : IToysDialog<UikitLayoutPickerDatetimeBinding, DatetimePicker.Builder>() {

    companion object {
        const val YEAR_MONTH_DAY = DateMode.YEAR_MONTH_DAY

        const val YEAR_MONTH = DateMode.YEAR_MONTH

        const val MONTH_DAY = DateMode.MONTH_DAY

        const val HOUR_24_NO_SECOND = TimeMode.HOUR_24_NO_SECOND

        const val HOUR_24_HAS_SECOND = TimeMode.HOUR_24_HAS_SECOND

        const val HOUR_12_NO_SECOND = TimeMode.HOUR_12_NO_SECOND

        const val HOUR_12_HAS_SECOND = TimeMode.HOUR_12_HAS_SECOND

        /**
         * 显示对话框
         */
        fun show(builder: Builder.() -> Unit) {
            val dialog = DatetimePicker()
            dialog.builder = Builder.create().apply(builder)
            dialog.showDialog()
        }
    }

    /**
     * Dialog builder
     */
    private lateinit var builder: Builder

    class Builder : AbsDialog.Builder() {
        companion object {

            /**
             * 创建 builder
             */
            fun create(): Builder = Builder()
        }

        override var dialogGravity = Gravity.BOTTOM

        override var windowAnimationsId = R.style.IToysAndroid_Dialog_Animation_Translate1

        var dateMode = YEAR_MONTH_DAY

        var timeMode = HOUR_24_NO_SECOND

        /** 回调 */
        var callback: IDatetimeCallback? = null
    }

    override fun builder() = this.builder

    /** 年 */
    private var selectYear = 0

    /** 月 */
    private var selectMonth = 0

    /** 日 */
    private var selectDay = 0

    /** 时 */
    private var selectHour = 0

    /** 分 */
    private var selectMinute = 0

    /** 秒 */
    private var selectSeconds = 0

    override fun createViewBinding(inflater: LayoutInflater) = UikitLayoutPickerDatetimeBinding.inflate(inflater)

    override fun initialize() {
        if (builder.title.isNotBlank()) {
            binding?.title?.text = builder.title
        }

        binding?.dateWheel?.apply {
            setDateMode(builder.dateMode)
            setOnDateSelectedListener { year, month, day ->
                selectYear = year
                selectMonth = month
                selectDay = day
            }
            setRange(DateEntity.target(2023, 1, 1), DateEntity.today())
        }

        binding?.timeWheel?.apply {
            setTimeMode(builder.timeMode)

            setOnTimeSelectedListener { hour, minute, second ->
                selectHour = hour
                selectMinute = minute
                selectSeconds = second
            }
        }

        binding?.close?.doOnClick { dismiss() }

        binding?.confirm?.doOnClick {
            builder.callback?.onResult(selectYear, selectMonth, selectDay, selectHour, selectMinute, selectSeconds)
            dismiss()
        }
    }

    /**
     * date wheel view
     */
    fun dateWheelView() = binding?.dateWheel

    /**
     * time wheel view
     */
    fun timeWheelView() = binding?.timeWheel

    /**
     * 回调
     */
    fun interface IDatetimeCallback {

        /**
         * 日期结果
         */
        fun onResult(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int)
    }
}