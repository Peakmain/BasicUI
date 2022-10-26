package com.peakmain.ui.utils.wheel

import android.view.Gravity
import com.peakmain.ui.utils.wheel.LunarCalendarUtil.lunarToSolar
import com.peakmain.ui.utils.wheel.LunarCalendarUtil.solarToLunar
import com.peakmain.ui.wheelview.adapter.ArrayWheelAdapter
import com.peakmain.ui.wheelview.adapter.NumericWheelAdapter
import com.peakmain.ui.wheelview.view.WheelView
import com.peakmain.ui.wheelview.listener.OnItemSelectedListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * author:Peakmain
 * createTime:2021/5/17
 * mail:2726449200@qq.com
 * describe：滚轮时间选择器辅助工具
 */
class WheelTimeUtil(
        private val mWheelViewYear: WheelView,
        private val mWheelViewMonth: WheelView,
        private val mWheelViewDay: WheelView
) {
    var startYear = DEFAULT_START_YEAR
    var endYear = DEFAULT_END_YEAR
    private var startMonth = DEFAULT_START_MONTH
    private var endMonth = DEFAULT_END_MONTH
    private var startDay = DEFAULT_START_DAY
    private var endDay = DEFAULT_END_DAY //表示31天的
    private var currentYear = 0


    var isLunarMode = false
    private var mSelectChangeCallback: ISelectTimeCallback? = null

    /**
     * 设置时间
     *
     * @param year
     * @param month
     * @param day
     */
    fun setPicker(year: Int, month: Int, day: Int) {
        if (isLunarMode) {
            val lunar = solarToLunar(year, month + 1, day)
            setLunar(lunar[0], lunar[1] - 1, lunar[2])
        } else {
            setSolar(year, month, day)
        }
    }

    /**
     * 设置农历
     *
     * @param year
     * @param month
     * @param day
     */
    private fun setLunar(year: Int, month: Int, day: Int) {
        // 年
        mWheelViewYear.adapter = ArrayWheelAdapter(
            DateUtils.getYears(
                startYear,
                endYear
            )
        ) // 设置"年"的显示数据
        mWheelViewYear.setLabel("") // 添加文字
        mWheelViewYear.currentItem = year - startYear // 初始化时显示的数据
        mWheelViewYear.setGravity(Gravity.CENTER)

        // 月
        mWheelViewMonth.adapter = ArrayWheelAdapter(
            DateUtils.getMonths(
                year
            )
        )
        mWheelViewMonth.setLabel("")
        mWheelViewMonth.currentItem = month
        mWheelViewMonth.setGravity(Gravity.CENTER)

        // 日
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (DateUtils.leapMonth(year) == 0) {
            mWheelViewDay.adapter = ArrayWheelAdapter(
                DateUtils.getLunarDays(
                    DateUtils.monthDays(year, month)
                )
            )
        } else {
            mWheelViewDay.adapter = ArrayWheelAdapter(
                DateUtils.getLunarDays(
                    DateUtils.leapDays(year)
                )
            )
        }
        mWheelViewDay.setLabel("")
        mWheelViewDay.currentItem = day - 1
        mWheelViewDay.setGravity(Gravity.CENTER)

        // 添加"年"监听
        mWheelViewYear.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(index: Int) {
                val yearNumber = index + startYear
                // 判断是不是闰年,来确定月和日的选择
                mWheelViewMonth.adapter = ArrayWheelAdapter(
                    DateUtils.getMonths(
                        yearNumber
                    )
                )
                if (DateUtils.leapMonth(yearNumber) != 0 && mWheelViewMonth.currentItem > DateUtils.leapMonth(
                        yearNumber
                    ) - 1
                ) {
                    mWheelViewMonth.currentItem = mWheelViewMonth.currentItem + 1
                } else {
                    mWheelViewMonth.currentItem = mWheelViewMonth.currentItem
                }
                val maxItem: Int
                if (DateUtils.leapMonth(yearNumber) != 0 && mWheelViewMonth.currentItem > DateUtils.leapMonth(
                        yearNumber
                    ) - 1
                ) {
                    if (mWheelViewMonth.currentItem == DateUtils.leapMonth(
                            yearNumber
                        ) + 1
                    ) {
                        mWheelViewDay.adapter = ArrayWheelAdapter(
                            DateUtils.getLunarDays(
                                DateUtils.leapDays(yearNumber)
                            )
                        )
                        maxItem = DateUtils.leapDays(yearNumber)
                    } else {
                        mWheelViewDay.adapter = ArrayWheelAdapter(
                            DateUtils.getLunarDays(
                                DateUtils.monthDays(
                                    yearNumber,
                                    mWheelViewMonth.currentItem
                                )
                            )
                        )
                        maxItem = DateUtils.monthDays(
                            yearNumber,
                            mWheelViewMonth.currentItem
                        )
                    }
                } else {
                    mWheelViewDay.adapter = ArrayWheelAdapter(
                        DateUtils.getLunarDays(
                            DateUtils.monthDays(
                                yearNumber,
                                mWheelViewMonth.currentItem + 1
                            )
                        )
                    )
                    maxItem = DateUtils.monthDays(
                        yearNumber,
                        mWheelViewMonth.currentItem + 1
                    )
                }
                if (mWheelViewDay.currentItem > maxItem - 1) {
                    mWheelViewDay.currentItem = maxItem - 1
                }
                if (mSelectChangeCallback != null) {
                    mSelectChangeCallback!!.onTimeSelectChanged()
                }
            }
        })

        // 添加"月"监听
        mWheelViewMonth.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(index: Int) {
                val yearNumber = mWheelViewYear.currentItem + startYear
                val maxItem: Int
                if (DateUtils.leapMonth(yearNumber) != 0 && index > DateUtils.leapMonth(
                        yearNumber
                    ) - 1
                ) {
                    if (mWheelViewMonth.currentItem == DateUtils.leapMonth(
                            yearNumber
                        ) + 1
                    ) {
                        mWheelViewDay.adapter = ArrayWheelAdapter(
                            DateUtils.getLunarDays(
                                DateUtils.leapDays(yearNumber)
                            )
                        )
                        maxItem = DateUtils.leapDays(yearNumber)
                    } else {
                        mWheelViewDay.adapter = ArrayWheelAdapter(
                            DateUtils.getLunarDays(
                                DateUtils.monthDays(
                                    yearNumber,
                                    index
                                )
                            )
                        )
                        maxItem =
                            DateUtils.monthDays(yearNumber, index)
                    }
                } else {
                    mWheelViewDay.adapter = ArrayWheelAdapter(
                        DateUtils.getLunarDays(
                            DateUtils.monthDays(
                                yearNumber,
                                index + 1
                            )
                        )
                    )
                    maxItem =
                        DateUtils.monthDays(yearNumber, index + 1)
                }
                if (mWheelViewDay.currentItem > maxItem - 1) {
                    mWheelViewDay.currentItem = maxItem - 1
                }
                if (mSelectChangeCallback != null) {
                    mSelectChangeCallback!!.onTimeSelectChanged()
                }
            }
        })
        setChangedListener(mWheelViewDay)
    }

    /**
     * 设置公历
     *
     * @param year
     * @param month
     * @param day
     */
    private fun setSolar(year: Int, month: Int, day: Int) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        val monthsBig =
            arrayOf("1", "3", "5", "7", "8", "10", "12")
        val monthsLittle = arrayOf("4", "6", "9", "11")
        val listBig =
            listOf(*monthsBig)
        val listLittle =
            listOf(*monthsLittle)
        currentYear = year
        // 年
        mWheelViewYear.adapter = NumericWheelAdapter(startYear, endYear) // 设置"年"的显示数据
        mWheelViewYear.currentItem = year - startYear // 初始化时显示的数据
        mWheelViewYear.setGravity(Gravity.CENTER)
        // 月
        if (startYear == endYear) { //开始年等于终止年
            mWheelViewMonth.adapter = NumericWheelAdapter(startMonth, endMonth)
            mWheelViewMonth.currentItem = month + 1 - startMonth
        } else if (year == startYear) {
            //起始日期的月份控制
            mWheelViewMonth.adapter = NumericWheelAdapter(startMonth, 12)
            mWheelViewMonth.currentItem = month + 1 - startMonth
        } else if (year == endYear) {
            //终止日期的月份控制
            mWheelViewMonth.adapter = NumericWheelAdapter(1, endMonth)
            mWheelViewMonth.currentItem = month
        } else {
            mWheelViewMonth.adapter = NumericWheelAdapter(1, 12)
            mWheelViewMonth.currentItem = month
        }
        mWheelViewMonth.setGravity(Gravity.CENTER)
        mWheelViewDay.setGravity(Gravity.CENTER)
        // 日
        if (startYear == endYear && startMonth == endMonth) {
            if (listBig.contains((month + 1).toString())) {
                if (endDay > 31) {
                    endDay = 31
                }
                mWheelViewDay.adapter = NumericWheelAdapter(startDay, endDay)
            } else if (listLittle.contains((month + 1).toString())) {
                if (endDay > 30) {
                    endDay = 30
                }
                mWheelViewDay.adapter = NumericWheelAdapter(startDay, endDay)
            } else {
                // 闰年
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29
                    }
                    mWheelViewDay.adapter = NumericWheelAdapter(startDay, endDay)
                } else {
                    if (endDay > 28) {
                        endDay = 28
                    }
                    mWheelViewDay.adapter = NumericWheelAdapter(startDay, endDay)
                }
            }
            mWheelViewDay.currentItem = day - startDay
        } else if (year == startYear && month + 1 == startMonth) {
            // 起始日期的天数控制
            if (listBig.contains((month + 1).toString())) {
                mWheelViewDay.adapter = NumericWheelAdapter(startDay, 31)
            } else if (listLittle.contains((month + 1).toString())) {
                mWheelViewDay.adapter = NumericWheelAdapter(startDay, 30)
            } else {
                // 闰年
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                    mWheelViewDay.adapter = NumericWheelAdapter(startDay, 29)
                } else {
                    mWheelViewDay.adapter = NumericWheelAdapter(startDay, 28)
                }
            }
            mWheelViewDay.currentItem = day - startDay
        } else if (year == endYear && month + 1 == endMonth) {
            // 终止日期的天数控制
            if (listBig.contains((month + 1).toString())) {
                if (endDay > 31) {
                    endDay = 31
                }
                mWheelViewDay.adapter = NumericWheelAdapter(1, endDay)
            } else if (listLittle.contains((month + 1).toString())) {
                if (endDay > 30) {
                    endDay = 30
                }
                mWheelViewDay.adapter = NumericWheelAdapter(1, endDay)
            } else {
                // 闰年
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29
                    }
                    mWheelViewDay.adapter = NumericWheelAdapter(1, endDay)
                } else {
                    if (endDay > 28) {
                        endDay = 28
                    }
                    mWheelViewDay.adapter = NumericWheelAdapter(1, endDay)
                }
            }
            mWheelViewDay.currentItem = day - 1
        } else {
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (listBig.contains((month + 1).toString())) {
                mWheelViewDay.adapter = NumericWheelAdapter(1, 31)
            } else if (listLittle.contains((month + 1).toString())) {
                mWheelViewDay.adapter = NumericWheelAdapter(1, 30)
            } else {
                // 闰年
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                    mWheelViewDay.adapter = NumericWheelAdapter(1, 29)
                } else {
                    mWheelViewDay.adapter = NumericWheelAdapter(1, 28)
                }
            }
            mWheelViewDay.currentItem = day - 1
        }

        // 添加"年"监听
        mWheelViewYear.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(index: Int) {
                val yearNumber = index + startYear
                currentYear = yearNumber
                var currentMonthItem = mWheelViewMonth.currentItem //记录上一次的item位置
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (startYear == endYear) {
                    //重新设置月份
                    mWheelViewMonth.adapter = NumericWheelAdapter(startMonth, endMonth)
                    if (currentMonthItem > mWheelViewMonth.adapter.itemsCount - 1) {
                        currentMonthItem = mWheelViewMonth.adapter.itemsCount - 1
                        mWheelViewMonth.currentItem = currentMonthItem
                    }
                    val monthNum = currentMonthItem + startMonth
                    if (startMonth == endMonth) {
                        //重新设置日
                        setReDay(yearNumber, monthNum, startDay, endDay, listBig, listLittle)
                    } else if (monthNum == startMonth) {
                        //重新设置日
                        setReDay(yearNumber, monthNum, startDay, 31, listBig, listLittle)
                    } else if (monthNum == endMonth) {
                        setReDay(yearNumber, monthNum, 1, endDay, listBig, listLittle)
                    } else { //重新设置日
                        setReDay(yearNumber, monthNum, 1, 31, listBig, listLittle)
                    }
                } else if (yearNumber == startYear) { //等于开始的年
                    //重新设置月份
                    mWheelViewMonth.adapter = NumericWheelAdapter(startMonth, 12)
                    if (currentMonthItem > mWheelViewMonth.adapter.itemsCount - 1) {
                        currentMonthItem = mWheelViewMonth.adapter.itemsCount - 1
                        mWheelViewMonth.currentItem = currentMonthItem
                    }
                    val month = currentMonthItem + startMonth
                    if (month == startMonth) {
                        //重新设置日
                        setReDay(yearNumber, month, startDay, 31, listBig, listLittle)
                    } else {
                        //重新设置日
                        setReDay(yearNumber, month, 1, 31, listBig, listLittle)
                    }
                } else if (yearNumber == endYear) {
                    //重新设置月份
                    mWheelViewMonth.adapter = NumericWheelAdapter(1, endMonth)
                    if (currentMonthItem > mWheelViewMonth.adapter.itemsCount - 1) {
                        currentMonthItem = mWheelViewMonth.adapter.itemsCount - 1
                        mWheelViewMonth.currentItem = currentMonthItem
                    }
                    val monthNum = currentMonthItem + 1
                    if (monthNum == endMonth) {
                        //重新设置日
                        setReDay(yearNumber, monthNum, 1, endDay, listBig, listLittle)
                    } else {
                        //重新设置日
                        setReDay(yearNumber, monthNum, 1, 31, listBig, listLittle)
                    }
                } else {
                    //重新设置月份
                    mWheelViewMonth.adapter = NumericWheelAdapter(1, 12)
                    //重新设置日
                    setReDay(yearNumber, mWheelViewMonth.currentItem + 1, 1, 31, listBig, listLittle)
                }
                if (mSelectChangeCallback != null) {
                    mSelectChangeCallback!!.onTimeSelectChanged()
                }
            }
        })


        // 添加"月"监听
        mWheelViewMonth.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(index: Int) {
                var monthNumber = index + 1
                if (startYear == endYear) {
                    monthNumber = monthNumber + startMonth - 1
                    if (startMonth == endMonth) {
                        //重新设置日
                        setReDay(currentYear, monthNumber, startDay, endDay, listBig, listLittle)
                    } else if (startMonth == monthNumber) {

                        //重新设置日
                        setReDay(currentYear, monthNumber, startDay, 31, listBig, listLittle)
                    } else if (endMonth == monthNumber) {
                        setReDay(currentYear, monthNumber, 1, endDay, listBig, listLittle)
                    } else {
                        setReDay(currentYear, monthNumber, 1, 31, listBig, listLittle)
                    }
                } else if (currentYear == startYear) {
                    monthNumber = monthNumber + startMonth - 1
                    if (monthNumber == startMonth) {
                        //重新设置日
                        setReDay(currentYear, monthNumber, startDay, 31, listBig, listLittle)
                    } else {
                        //重新设置日
                        setReDay(currentYear, monthNumber, 1, 31, listBig, listLittle)
                    }
                } else if (currentYear == endYear) {
                    if (monthNumber == endMonth) {
                        //重新设置日
                        setReDay(
                            currentYear,
                            mWheelViewMonth.currentItem + 1,
                            1,
                            endDay,
                            listBig,
                            listLittle
                        )
                    } else {
                        setReDay(
                            currentYear,
                            mWheelViewMonth.currentItem + 1,
                            1,
                            31,
                            listBig,
                            listLittle
                        )
                    }
                } else {
                    //重新设置日
                    setReDay(currentYear, monthNumber, 1, 31, listBig, listLittle)
                }
                if (mSelectChangeCallback != null) {
                    mSelectChangeCallback!!.onTimeSelectChanged()
                }
            }
        })
        setChangedListener(mWheelViewDay)
    }

    private fun setChangedListener(wheelView: WheelView) {
        if (mSelectChangeCallback != null) {
            wheelView.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(index: Int) {
                    mSelectChangeCallback!!.onTimeSelectChanged()
                }
            })
        }
    }

    private fun setReDay(
        yearNumber: Int,
        monthNum: Int,
        startD: Int,
        endD: Int,
        list_big: List<String>,
        list_little: List<String>
    ) {
        var end = endD
        var currentItem = mWheelViewDay.currentItem
        //        int maxItem;
        if (list_big.contains(monthNum.toString())) {
            if (end > 31) {
                end = 31
            }
            mWheelViewDay.adapter = NumericWheelAdapter(startD, end)
        } else if (list_little.contains(monthNum.toString())) {
            if (end > 30) {
                end = 30
            }
            mWheelViewDay.adapter = NumericWheelAdapter(startD, end)
        } else {
            if (yearNumber % 4 == 0 && yearNumber % 100 != 0
                || yearNumber % 400 == 0
            ) {
                if (end > 29) {
                    end = 29
                }
                mWheelViewDay.adapter = NumericWheelAdapter(startD, end)
            } else {
                if (end > 28) {
                    end = 28
                }
                mWheelViewDay.adapter = NumericWheelAdapter(startD, end)
            }
        }
        if (currentItem > mWheelViewDay.adapter.itemsCount - 1) {
            currentItem = mWheelViewDay.adapter.itemsCount - 1
            mWheelViewDay.currentItem = currentItem
        }
    }

    //如果是农历 返回对应的公历时间
    val time: String
        get() {
            if (isLunarMode) {
                //如果是农历 返回对应的公历时间
                return lunarTime
            }
            var time: String? = null
            time = if (currentYear == startYear) {
                if (mWheelViewMonth.currentItem + startMonth == startMonth) {
                    appendCurrentTime(
                        mWheelViewYear.currentItem + startYear,
                        mWheelViewMonth.currentItem + startMonth,
                        mWheelViewDay.currentItem + startDay
                    )
                } else {
                    appendCurrentTime(
                        mWheelViewYear.currentItem + startYear,
                        mWheelViewMonth.currentItem + startMonth,
                        mWheelViewDay.currentItem + 1
                    )
                }
            } else {
                val year = mWheelViewYear.currentItem + startYear
                val month = mWheelViewMonth.currentItem + 1
                val day = mWheelViewDay.currentItem + 1
                appendCurrentTime(year, month, day)
            }
            return time
        }

    fun getCurrentYear(): Int {
        return mWheelViewYear.currentItem + startYear
    }

    val currentMonth: Int
        get() = mWheelViewMonth.currentItem + 1

    private fun appendCurrentTime(year: Int, month: Int, day: Int): String {
        val sb = StringBuilder()
        sb.append(year).append("-")
        if (month < 10) {
            sb.append("0").append(month)
        } else {
            sb.append(month)
        }
        sb.append("-")
        if (day < 10) {
            sb.append("0").append(day)
        } else {
            sb.append(day)
        }
        return sb.toString()
    }

    /**
     * 农历返回对应的公历时间
     *
     * @return
     */
    private val lunarTime: String
        get() {
            val sb = StringBuilder()
            val year = mWheelViewYear.currentItem + startYear
            var month = 1
            var isLeapMonth = false
            if (DateUtils.leapMonth(year) == 0) {
                month = mWheelViewMonth.currentItem + 1
            } else {
                if (mWheelViewMonth.currentItem + 1 - DateUtils.leapMonth(
                        year
                    ) <= 0
                ) {
                    month = mWheelViewMonth.currentItem + 1
                } else if (mWheelViewMonth.currentItem + 1 - DateUtils.leapMonth(
                        year
                    ) == 1
                ) {
                    month = mWheelViewMonth.currentItem
                    isLeapMonth = true
                } else {
                    month = mWheelViewMonth.currentItem
                }
            }
            val day = mWheelViewDay.currentItem + 1
            val solar = lunarToSolar(year, month, day, isLeapMonth)
            sb.append(solar[0]).append("-")
                .append(solar[1]).append("-")
                .append(solar[2]).append(" ")
            return sb.toString()
        }

    fun setRangDate(startDate: Calendar?, endDate: Calendar?) {
        if (startDate == null && endDate != null) {
            val year = endDate[Calendar.YEAR]
            val month = endDate[Calendar.MONTH] + 1
            val day = endDate[Calendar.DAY_OF_MONTH]
            if (year > startYear) {
                endYear = year
                endMonth = month
                endDay = day
            } else if (year == startYear) {
                if (month > startMonth) {
                    endYear = year
                    endMonth = month
                    endDay = day
                } else if (month == startMonth) {
                    if (day > startDay) {
                        endYear = year
                        endMonth = month
                        endDay = day
                    }
                }
            }
        } else if (startDate != null && endDate == null) {
            val year = startDate[Calendar.YEAR]
            val month = startDate[Calendar.MONTH] + 1
            val day = startDate[Calendar.DAY_OF_MONTH]
            if (year < endYear) {
                startMonth = month
                startDay = day
                startYear = year
            } else if (year == endYear) {
                if (month < endMonth) {
                    startMonth = month
                    startDay = day
                    startYear = year
                } else if (month == endMonth) {
                    if (day < endDay) {
                        startMonth = month
                        startDay = day
                        startYear = year
                    }
                }
            }
        } else if (startDate != null && endDate != null) {
            startYear = startDate[Calendar.YEAR]
            endYear = endDate[Calendar.YEAR]
            startMonth = startDate[Calendar.MONTH] + 1
            endMonth = endDate[Calendar.MONTH] + 1
            startDay = startDate[Calendar.DAY_OF_MONTH]
            endDay = endDate[Calendar.DAY_OF_MONTH]
        }
    }

    /**
     * 设置标签
     */
    fun setLabel(year: String?, month: String?, day: String?) {
        mWheelViewYear.setLabel(year)
        mWheelViewMonth.setLabel(month)
        mWheelViewDay.setLabel(day)
    }

    /**
     * @param isCenterLabel 是否只显示中间选中项的
     */
    fun isCenterLabel(isCenterLabel: Boolean) {
        mWheelViewDay.isCenterLabel(isCenterLabel)
        mWheelViewMonth.isCenterLabel(isCenterLabel)
        mWheelViewYear.isCenterLabel(isCenterLabel)
    }

    fun setSelectChangeCallback(mSelectChangeCallback: ISelectTimeCallback?) {
        this.mSelectChangeCallback = mSelectChangeCallback
    }

    interface ISelectTimeCallback {
        fun onTimeSelectChanged()
    }

    companion object {
        var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        private const val DEFAULT_START_YEAR = 1900
        private const val DEFAULT_END_YEAR = 2100
        private const val DEFAULT_START_MONTH = 1
        private const val DEFAULT_END_MONTH = 12
        private const val DEFAULT_START_DAY = 1
        private const val DEFAULT_END_DAY = 31
    }

}