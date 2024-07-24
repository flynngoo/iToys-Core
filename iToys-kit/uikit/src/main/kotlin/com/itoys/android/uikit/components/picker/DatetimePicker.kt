package com.itoys.android.uikit.components.picker

import android.view.Gravity
import android.view.LayoutInflater
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.entity.TimeEntity
import com.itoys.android.uikit.R
import com.itoys.android.uikit.components.dialog.AbsDialog
import com.itoys.android.uikit.components.dialog.IToysDialog
import com.itoys.android.uikit.databinding.UikitLayoutPickerDatetimeBinding
import com.itoys.android.utils.expansion.color
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.isNotBlank
import com.itoys.android.utils.expansion.then

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

        /** 从今天开始 */
        var fromToday = true

        /** 到今天结束：最大日期只能选到今天 */
        var toToday = true

        /** 起始年份 */
        var startYear = 1900

        /** 默认年份 */
        var defaultYear = 0

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

            val startingDate: DateEntity
            var endingDate: DateEntity

            val today = DateEntity.today()

            when {
                builder.fromToday -> {
                    startingDate = today
                    endingDate = DateEntity.target(startingDate.year + 100, 12, 31)
                }

                else -> {
                    startingDate = DateEntity.target(builder.startYear, 1, 1)
                    endingDate = today

                    if (!builder.toToday) {
                        endingDate = DateEntity.target(endingDate.year + 100, 12, 31)
                    }
                }
            }
            setRange(startingDate, endingDate)

            setDefaultValue(
                DateEntity.target(
                    (builder.defaultYear > 0).then(
                        { builder.defaultYear },
                        { today.year }
                    ), today.month, today.day
                )
            )

            selectYear = selectedYear
            selectMonth = selectedMonth
            selectDay = selectedDay
        }

        binding?.timeWheel?.apply {
            setTimeMode(builder.timeMode)

            setDefaultValue(TimeEntity.now())

            setOnTimeSelectedListener { hour, minute, second ->
                selectHour = hour
                selectMinute = minute
                selectSeconds = second
            }

            selectHour = selectedHour
            selectMinute = selectedMinute
            selectSeconds = selectedSecond
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