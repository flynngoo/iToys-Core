package com.itoys.android.uikit.components.picker

import android.view.Gravity
import android.view.LayoutInflater
import com.itoys.android.uikit.R
import com.itoys.android.uikit.components.dialog.AbsDialog
import com.itoys.android.uikit.components.dialog.IToysDialog
import com.itoys.android.uikit.databinding.UikitLayoutPickerCalendarBinding
import com.itoys.android.utils.TimeUtils
import com.itoys.android.utils.expansion.doOnClick

/**
 * @Author Gu Fanfan
 * @Email fanfan.work@outlook.com
 * @Date 2023/12/26
 */
class CalendarPicker : IToysDialog<UikitLayoutPickerCalendarBinding, CalendarPicker.Builder>() {

    companion object {
        /**
         * 显示对话框
         */
        fun show(builder: Builder.() -> Unit) {
            val dialog = CalendarPicker()
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

        var datePattern = "yyyy-MM-dd"

        /** 时间范围选择 */
        var rangeMode = false

        var callback: IDateCallback? = null
    }

    override fun builder() = this.builder

    override fun createViewBinding(inflater: LayoutInflater) = UikitLayoutPickerCalendarBinding.inflate(inflater)

    override fun initialize() {
        binding?.title?.text = builder.title
        binding?.calendar?.setRangeMode(builder.rangeMode)
        binding?.close?.doOnClick { dismiss() }

        binding?.confirm?.doOnClick {
            if (builder.rangeMode) {
                val dates = binding?.calendar?.rangeCalendar()
                if (!dates.isNullOrEmpty()) {
                    val startDate = dates.first()?.toCalendar()
                    val endDate = dates.last()?.toCalendar()

                    builder.callback?.onRangeResult(
                        TimeUtils.calendar2String(startDate, builder.datePattern),
                        TimeUtils.calendar2String(endDate, builder.datePattern),
                        dates.size,
                    )
                }
            } else {
                val date = binding?.calendar?.calendarData()
                builder.callback?.onResult(TimeUtils.calendar2String(date, builder.datePattern))
            }

            dismiss()
        }
    }

    abstract class IDateCallback {

        /**
         * 日期结果
         */
        open fun onResult(data: String) {}

        /**
         * 日期范围结果
         */
        open fun onRangeResult(startDate: String, endDate: String, days: Int) {}
    }
}