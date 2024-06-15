package com.itoys.android.uikit.components.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.itoys.android.uikit.R
import com.itoys.android.uikit.databinding.UikitLayoutCalendarBinding
import com.itoys.android.utils.TimeUtils
import com.itoys.android.utils.expansion.invalid
import com.itoys.android.utils.expansion.padZeroes
import com.itoys.android.utils.expansion.then

/**
 * @Author Gu Fanfan
 * @Email fanfan.worker@gmail.com
 * @Date 2024/6/14
 */
class IToysCalendar(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val binding by lazy {
        UikitLayoutCalendarBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    /**
     * 日期格式
     */
    private var datePattern = ""

    /**
     * 日历回调
     */
    private var calendarCallback: ICalendarCallback? = null

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.IToysCalendar)
        datePattern = ta.getString(R.styleable.IToysCalendar_iToysDatePattern).invalid("yyyy/MM/dd")
        ta.recycle()

        binding.date.text = context.getString(
            R.string.uikit_date_year_month, binding.calendar.curYear, binding.calendar.curMonth.padZeroes()
        )

        binding.calendar.setOnMonthChangeListener { year, month ->
            binding.date.text = context.getString(
                R.string.uikit_date_year_month, year, month.padZeroes()
            )
        }

        binding.calendar.setOnCalendarSelectListener(object : CalendarView.OnCalendarSelectListener {
            override fun onCalendarOutOfRange(calendar: Calendar?) {
                /* 空 */
            }

            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                calendarCallback?.onSingleCalendar(TimeUtils.calendar2String(calendar?.toCalendar(), datePattern))
            }
        })

        binding.calendar.setOnCalendarRangeSelectListener(object : CalendarView.OnCalendarRangeSelectListener {
            override fun onCalendarSelectOutOfRange(calendar: Calendar?) {
                /* 超出范围提示 */
            }

            override fun onSelectOutOfRange(calendar: Calendar?, isOutOfMinRange: Boolean) {
                /* 超出范围提示 */
            }

            override fun onCalendarRangeSelect(calendar: Calendar?, isEnd: Boolean) {
                val date = TimeUtils.calendar2String(calendar?.toCalendar(), datePattern)

                isEnd.then(
                    { calendarCallback?.onRangeEndCalendar(date) },
                    { calendarCallback?.onRangeStartCalendar(date) },
                )
            }
        })

        binding.calendar
    }

    /**
     * 设置日历模式
     *
     * @param isRange 是否为范围模式
     */
    fun setRangeMode(isRange: Boolean) {
        if (isRange) {
            binding.calendar.setSelectRangeMode()
            binding.calendar.setMonthView(SimpleRangeMonthView::class.java)
        } else {
            binding.calendar.setMonthView(SimpleMonthView::class.java)
            binding.calendar.setSelectSingleMode()
            binding.calendar.scrollToCurrent()
        }
    }

    /**
     * 设置日历回调
     */
    fun setCalendarCallback(callback: ICalendarCallback) {
        this.calendarCallback = callback
    }

    /**
     * 获取日历范围
     */
    fun rangeCalendar() = binding.calendar.selectCalendarRange

    /**
     * 获取日历数据
     */
    fun calendarData() = binding.calendar.selectedCalendar?.toCalendar()

    interface ICalendarCallback {

        /**
         * 单日选择
         */
        fun onSingleCalendar(date: String) {}

        /**
         * 时间范围：开始选择日历
         */
        fun onRangeStartCalendar(date: String) {}

        /**
         * 时间范围：结束选择日历
         */
        fun onRangeEndCalendar(date: String) {}
    }
}