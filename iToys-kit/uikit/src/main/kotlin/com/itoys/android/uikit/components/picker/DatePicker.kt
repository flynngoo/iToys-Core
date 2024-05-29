package com.itoys.android.uikit.components.picker

import android.view.Gravity
import android.view.LayoutInflater
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.itoys.android.uikit.R
import com.itoys.android.uikit.components.dialog.AbsDialog
import com.itoys.android.uikit.components.dialog.IToysDialog
import com.itoys.android.uikit.databinding.UikitLayoutPickerDateBinding
import com.itoys.android.utils.expansion.color
import com.itoys.android.utils.expansion.doOnClick
import com.itoys.android.utils.expansion.isNotBlank
import com.itoys.android.utils.expansion.then
import com.itoys.android.utils.expansion.visible

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/26
 */
class DatePicker : IToysDialog<UikitLayoutPickerDateBinding, DatePicker.Builder>() {

    companion object {
        const val YEAR_MONTH_DAY = DateMode.YEAR_MONTH_DAY

        const val YEAR_MONTH = DateMode.YEAR_MONTH

        const val MONTH_DAY = DateMode.MONTH_DAY

        /**
         * 显示对话框
         */
        fun show(builder: Builder.() -> Unit) {
            val dialog = DatePicker()
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

        var dateMode = DateMode.YEAR_MONTH_DAY

        /** 从今天开始 */
        var fromToday = true

        /** 到今天结束：最大日期只能选到今天 */
        var toToday = true

        /** 显示长期有效，如果选择长期有效返回：9999-12-31 */
        var showLongTermValidity = false

        /** 起始年份 */
        var startYear = 1900

        /** 默认年份 */
        var defaultYear = 0

        /** 回调 */
        var callback: IDateCallback? = null
    }

    override fun builder() = this.builder

    /** 年 */
    private var selectYear = 0

    /** 月 */
    private var selectMonth = 0

    /** 日 */
    private var selectDay = 0

    /** 长期有效 */
    private var longTermValidity = false

    override fun createViewBinding(inflater: LayoutInflater) = UikitLayoutPickerDateBinding.inflate(inflater)

    override fun initialize() {
        if (builder.title.isNotBlank()) {
            binding?.title?.text = builder.title
        }

        if (builder.showLongTermValidity) {
            binding?.longTermValidity?.visible()

            binding?.longTermValidity?.setOnCheckedChangeListener { _, isChecked ->
                longTermValidity = isChecked
            }
        }

        binding?.dateWheel?.apply {
            setDateMode(builder.dateMode)
            setDateLabel("年", "月", "日")
            yearLabelView.setTextColor(context.color(R.color.uikit_colorful_333333))
            monthLabelView.setTextColor(context.color(R.color.uikit_colorful_333333))
            dayLabelView.setTextColor(context.color(R.color.uikit_colorful_333333))
            setOnDateSelectedListener { year, month, day ->
                selectYear = year
                selectMonth = month
                selectDay = day
            }

            val startingDate: DateEntity
            var endingDate: DateEntity

            when {
                builder.fromToday -> {
                    startingDate = DateEntity.today()
                    endingDate = DateEntity.target(startingDate.year + 100, 12, 31)
                }

                else -> {
                    startingDate = DateEntity.target(builder.startYear, 1, 1)
                    endingDate = DateEntity.today()

                    if (!builder.toToday) {
                        endingDate = DateEntity.target(endingDate.year + 100, 12, 31)
                    }
                }
            }
            setRange(startingDate, endingDate)

            setDefaultValue(
                DateEntity.target(
                    (builder.defaultYear > 0).then(
                        builder.defaultYear,
                        startingDate.year
                    ), startingDate.month, startingDate.day
                )
            )

            selectYear = selectedYear
            selectMonth = selectedMonth
            selectDay = selectedDay
        }

        binding?.close?.doOnClick { dismiss() }

        binding?.confirm?.doOnClick {
            if (longTermValidity) {
                // 长期有效
                selectYear = 9999
                selectMonth = 12
                selectDay = 31
            }

            builder.callback?.onResult(selectYear, selectMonth, selectDay)
            dismiss()
        }
    }

    /**
     * date wheel view
     */
    fun dateWheelView() = binding?.dateWheel

    /**
     * 回调
     */
    fun interface IDateCallback {

        /**
         * 日期结果
         */
        fun onResult(year: Int, month: Int, day: Int)
    }
}