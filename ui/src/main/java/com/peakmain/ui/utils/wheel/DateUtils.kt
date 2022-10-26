package com.peakmain.ui.utils.wheel

import java.text.SimpleDateFormat
import java.util.*

/**
 * author:Peakmain
 * createTime:2021/5/17
 * mail:2726449200@qq.com
 * describe：时间处理工具类
 */
class DateUtils {
    /**
     * 传出农历.year0 .month1 .day2 .yearCyl3 .monCyl4 .dayCyl5 .isLeap6
     *
     * @param y 年
     * @param m 月
     * @return 传出农历
     */
    private fun lunar(y: Int, m: Int): LongArray {
        val nongDate = LongArray(7)
        var temp = 0
        val leap: Int
        val baseDate = GregorianCalendar(1900 + 1900, 1, 31).time
        val objDate = GregorianCalendar(y + 1900, m, 1).time
        var offset = (objDate.time - baseDate.time) / 86400000L
        if (y < 2000) offset += year19[m - 1]
        if (y > 2000) offset += year20[m - 1]
        if (y == 2000) offset += year2000[m - 1]
        nongDate[5] = offset + 40
        nongDate[4] = 14
        var i = 1900
        while (i < 2050 && offset > 0) {
            temp = lYearDays(i)
            offset -= temp.toLong()
            nongDate[4] += 12L
            i++
        }
        if (offset < 0) {
            offset += temp.toLong()
            i--
            nongDate[4] -= 12L
        }
        nongDate[0] = i.toLong()
        nongDate[3] = (i - 1864).toLong()
        leap = leapMonth(i) // 闰哪个月
        nongDate[6] = 0
        i = 1
        while (i < 13 && offset > 0) {

            // 闰月
            if (leap > 0 && i == leap + 1 && nongDate[6] == 0L) {
                --i
                nongDate[6] = 1
                temp =
                        leapDays(nongDate[0].toInt())
            } else {
                temp = monthDays(
                        nongDate[0].toInt(),
                        i
                )
            }
            // 解除闰月
            if (nongDate[6] == 1L && i == leap + 1) nongDate[6] = 0
            offset -= temp.toLong()
            if (nongDate[6] == 0L) nongDate[4]++
            i++
        }
        if (offset == 0L && leap > 0 && i == leap + 1) {
            if (nongDate[6] == 1L) {
                nongDate[6] = 0
            } else {
                nongDate[6] = 1
                --i
                --nongDate[4]
            }
        }
        if (offset < 0) {
            offset += temp.toLong()
            --i
            --nongDate[4]
        }
        nongDate[1] = i.toLong()
        nongDate[2] = offset + 1
        return nongDate
    }

    companion object {
        /**
         * <lunarInfo 数组值的计算原理>
         *
         * 0x代表十六进制，后面的五位数是十六进制数。
         * 举个例子: 1980年的数据是 0x095b0
         * 二进制: 0000 1001 0101 1011 0000
         * 1-4:   表示当年有无闰年，有的话，为闰月的月份，没有的话，为0。
         * 5-16:  为除了闰月外的正常月份是大月还是小月，1为30天，0为29天。
         * 注意:  从1月到12月对应的是第16位到第5位。
         * 17-20: 表示闰月是大月还是小月，仅当存在闰月的情况下有意义。
        </lunarInfo> */
        private val lunarInfo = longArrayOf(
            0x04bd8,
            0x04ae0,
            0x0a570,
            0x054d5,
            0x0d260,
            0x0d950,
            0x16554,
            0x056a0,
            0x09ad0,
            0x055d2,  //1900-1909
            0x04ae0,
            0x0a5b6,
            0x0a4d0,
            0x0d250,
            0x1d255,
            0x0b540,
            0x0d6a0,
            0x0ada2,
            0x095b0,
            0x14977,  //1910-1919
            0x04970,
            0x0a4b0,
            0x0b4b5,
            0x06a50,
            0x06d40,
            0x1ab54,
            0x02b60,
            0x09570,
            0x052f2,
            0x04970,  //1920-1929
            0x06566,
            0x0d4a0,
            0x0ea50,
            0x06e95,
            0x05ad0,
            0x02b60,
            0x186e3,
            0x092e0,
            0x1c8d7,
            0x0c950,  //1930-1939
            0x0d4a0,
            0x1d8a6,
            0x0b550,
            0x056a0,
            0x1a5b4,
            0x025d0,
            0x092d0,
            0x0d2b2,
            0x0a950,
            0x0b557,  //1940-1949
            0x06ca0,
            0x0b550,
            0x15355,
            0x04da0,
            0x0a5b0,
            0x14573,
            0x052b0,
            0x0a9a8,
            0x0e950,
            0x06aa0,  //1950-1959
            0x0aea6,
            0x0ab50,
            0x04b60,
            0x0aae4,
            0x0a570,
            0x05260,
            0x0f263,
            0x0d950,
            0x05b57,
            0x056a0,  //1960-1969
            0x096d0,
            0x04dd5,
            0x04ad0,
            0x0a4d0,
            0x0d4d4,
            0x0d250,
            0x0d558,
            0x0b540,
            0x0b6a0,
            0x195a6,  //1970-1979
            0x095b0,
            0x049b0,
            0x0a974,
            0x0a4b0,
            0x0b27a,
            0x06a50,
            0x06d40,
            0x0af46,
            0x0ab60,
            0x09570,  //1980-1989
            0x04af5,
            0x04970,
            0x064b0,
            0x074a3,
            0x0ea50,
            0x06b58,
            0x055c0,
            0x0ab60,
            0x096d5,
            0x092e0,  //1990-1999
            0x0c960,
            0x0d954,
            0x0d4a0,
            0x0da50,
            0x07552,
            0x056a0,
            0x0abb7,
            0x025d0,
            0x092d0,
            0x0cab5,  //2000-2009
            0x0a950,
            0x0b4a0,
            0x0baa4,
            0x0ad50,
            0x055d9,
            0x04ba0,
            0x0a5b0,
            0x15176,
            0x052b0,
            0x0a930,  //2010-2019
            0x07954,
            0x06aa0,
            0x0ad50,
            0x05b52,
            0x04b60,
            0x0a6e6,
            0x0a4e0,
            0x0d260,
            0x0ea65,
            0x0d530,  //2020-2029
            0x05aa0,
            0x076a3,
            0x096d0,
            0x04afb,
            0x04ad0,
            0x0a4d0,
            0x1d0b6,
            0x0d250,
            0x0d520,
            0x0dd45,  //2030-2039
            0x0b5a0,
            0x056d0,
            0x055b2,
            0x049b0,
            0x0a577,
            0x0a4b0,
            0x0aa50,
            0x1b255,
            0x06d20,
            0x0ada0,  //2040-2049
            0x14b63,
            0x09370,
            0x049f8,
            0x04970,
            0x064b0,
            0x168a6,
            0x0ea50,
            0x06b20,
            0x1a6c4,
            0x0aae0,  //2050-2059
            0x0a2e0,
            0x0d2e3,
            0x0c960,
            0x0d557,
            0x0d4a0,
            0x0da50,
            0x05d55,
            0x056a0,
            0x0a6d0,
            0x055d4,  //2060-2069
            0x052d0,
            0x0a9b8,
            0x0a950,
            0x0b4a0,
            0x0b6a6,
            0x0ad50,
            0x055a0,
            0x0aba4,
            0x0a5b0,
            0x052b0,  //2070-2079
            0x0b273,
            0x06930,
            0x07337,
            0x06aa0,
            0x0ad50,
            0x14b55,
            0x04b60,
            0x0a570,
            0x054e4,
            0x0d160,  //2080-2089
            0x0e968,
            0x0d520,
            0x0daa0,
            0x16aa6,
            0x056d0,
            0x04ae0,
            0x0a9d4,
            0x0a2d0,
            0x0d150,
            0x0f252,  //2090-2099
            0x0d520
        ) //2100
        private val year20 = intArrayOf(1, 4, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1)
        private val year19 = intArrayOf(0, 3, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0)
        private val year2000 = intArrayOf(0, 3, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1)
        val nStr1 = arrayOf(
            "", "正", "二", "三", "四",
            "五", "六", "七", "八", "九", "十", "冬", "腊"
        )
        private val Gan = arrayOf(
            "甲", "乙", "丙", "丁", "戊",
            "己", "庚", "辛", "壬", "癸"
        )
        private val Zhi = arrayOf(
            "子", "丑", "寅", "卯", "辰",
            "巳", "午", "未", "申", "酉", "戌", "亥"
        )
        private val Animals = arrayOf(
            "鼠", "牛", "虎", "兔",
            "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"
        )

        /**
         * 传回农历
         *
         * @param y 年的总天数
         * @return 农历
         */
        private fun lYearDays(y: Int): Int {
            var i: Int
            var sum = 348
            i = 0x8000
            while (i > 0x8) {
                if (lunarInfo[y - 1900] and i.toLong() != 0L) sum += 1
                i = i shr 1
            }
            return sum + leapDays(y)
        }

        /**
         * 传回农历
         *
         * @param y 年闰月的天数
         * @return 农历
         */
        fun leapDays(y: Int): Int {
            return if (leapMonth(y) != 0) {
                if (lunarInfo[y - 1900] and 0x10000 != 0L) 30 else 29
            } else 0
        }

        /**
         * 传回农历
         *
         * @param y 年闰哪个月 1-12 , 没闰传回 0
         * @return 农历
         */
        fun leapMonth(y: Int): Int {
            return (lunarInfo[y - 1900] and 0xf).toInt()
        }

        /**
         * 传回农历 y
         *
         * @param y y年m月的总天数
         * @param m y年m月的总天数
         * @return 农历
         */
        fun monthDays(y: Int, m: Int): Int {
            return if (lunarInfo[y - 1900] and (0x10000 shr m).toLong() == 0L) 29 else 30
        }

        /**
         * 传回农历
         *
         * @param y 年的生肖
         * @return
         */
        fun animalsYear(y: Int): String {
            return Animals[(y - 4) % 12]
        }

        /**
         * 传入
         *
         * @param num 月日的offset 传回干支,0是甲子
         * @return 干支
         */
        private fun cyclicalm(num: Int): String {
            return Gan[num % 10] + Zhi[num % 12]
        }

        /**
         * 传入 offset 传回干支
         *
         * @param y 0是甲子
         * @return 干支
         */
        fun cyclical(y: Int): String {
            val num = y - 1900 + 36
            return cyclicalm(num)
        }

        /**
         * 传出y年m月d日对应的农历.year0 .month1 .day2 .yearCyl3 .monCyl4 .dayCyl5 .isLeap6
         *
         * @param y 年
         * @param m 月
         * @param d 日
         * @return y年m月d日对应的农历
         */
        fun calElement(y: Int, m: Int, d: Int): LongArray {
            val nongDate = LongArray(7)
            var temp = 0
            val leap: Int
            val baseDate = GregorianCalendar(0 + 1900, 0, 31).time
            val objDate = GregorianCalendar(y, m - 1, d).time
            var offset = (objDate.time - baseDate.time) / 86400000L
            nongDate[5] = offset + 40
            nongDate[4] = 14
            var i = 1900
            while (i < 2050 && offset > 0) {
                temp = lYearDays(i)
                offset -= temp.toLong()
                nongDate[4] += 12L
                i++
            }
            if (offset < 0) {
                offset += temp.toLong()
                i--
                nongDate[4] -= 12L
            }
            nongDate[0] = i.toLong()
            nongDate[3] = (i - 1864).toLong()
            leap = leapMonth(i) // 闰哪个月
            nongDate[6] = 0
            i = 1
            while (i < 13 && offset > 0) {

                // 闰月
                if (leap > 0 && i == leap + 1 && nongDate[6] == 0L) {
                    --i
                    nongDate[6] = 1
                    temp =
                            leapDays(nongDate[0].toInt())
                } else {
                    temp = monthDays(
                            nongDate[0].toInt(),
                            i
                    )
                }
                // 解除闰月
                if (nongDate[6] == 1L && i == leap + 1) nongDate[6] = 0
                offset -= temp.toLong()
                if (nongDate[6] == 0L) nongDate[4]++
                i++
            }
            if (offset == 0L && leap > 0 && i == leap + 1) {
                if (nongDate[6] == 1L) {
                    nongDate[6] = 0
                } else {
                    nongDate[6] = 1
                    --i
                    --nongDate[4]
                }
            }
            if (offset < 0) {
                offset += temp.toLong()
                --i
                --nongDate[4]
            }
            nongDate[1] = i.toLong()
            nongDate[2] = offset + 1
            return nongDate
        }

        fun getChinaDate(day: Int): String {
            var a = ""
            if (day == 10) return "初十"
            if (day == 20) return "二十"
            if (day == 30) return "三十"
            val two = day / 10
            if (two == 0) a = "初"
            if (two == 1) a = "十"
            if (two == 2) a = "廿"
            if (two == 3) a = "三"
            when (day % 10) {
                1 -> a += "一"
                2 -> a += "二"
                3 -> a += "三"
                4 -> a += "四"
                5 -> a += "五"
                6 -> a += "六"
                7 -> a += "七"
                8 -> a += "八"
                9 -> a += "九"
            }
            return a
        }

        fun today(): String {
            val today =
                Calendar.getInstance(Locale.SIMPLIFIED_CHINESE)
            val year = today[Calendar.YEAR]
            val month = today[Calendar.MONTH] + 1
            val date = today[Calendar.DATE]
            val l =
                    calElement(year, month, date)
            var sToday: StringBuffer? = StringBuffer()
            return try {
                sToday!!.append(sdf.format(today.time))
                sToday.append(" 农历")
                sToday.append(cyclical(year))
                sToday.append('(')
                sToday.append(animalsYear(year))
                sToday.append(")年")
                sToday.append(nStr1[l[1].toInt()])
                sToday.append("月")
                sToday.append(getChinaDate(l[2].toInt()))
                sToday.toString()
            } finally {
                sToday = null
            }
        }

        fun oneDay(year: Int, month: Int, day: Int): String {
            //   Calendar today = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
            val l =
                    calElement(year, month, day)
            var sToday: StringBuffer? = StringBuffer()
            return try {
                //   sToday.append(sdf.format(today.getTime()));
                sToday!!.append(" 农历")
                sToday.append(cyclical(year))
                sToday.append('(')
                sToday.append(animalsYear(year))
                sToday.append(")年")
                sToday.append(nStr1[l[1].toInt()])
                sToday.append("月")
                sToday.append(getChinaDate(l[2].toInt()))
                sToday.toString()
            } finally {
                sToday = null
            }
        }

        private val sdf = SimpleDateFormat("yyyy年M月d日 EEEEE")

        /**
         * @param lunarYear 农历年份
         * @return String of Ganzhi: 甲子年
         * 甲乙丙丁戊己庚辛壬癸
         * 子丑寅卯辰巳无为申酉戌亥
         */
        fun getLunarYearText(lunarYear: Int): String {
            return Gan[(lunarYear - 4) % 10] + Zhi[(lunarYear - 4) % 12] + "年"
        }

        fun getYears(startYear: Int, endYear: Int): ArrayList<String> {
            val years = ArrayList<String>()
            for (i in startYear until endYear) {
                years.add(
                    String.format(
                        "%s(%d)",
                            getLunarYearText(i),
                        i
                    )
                )
            }
            return years
        }

        /**
         * 获取year年的所有月份
         *
         * @param year 年
         * @return 月份列表
         */
        fun getMonths(year: Int): ArrayList<String> {
            val baseMonths =
                ArrayList<String>()
            for (i in 1 until nStr1.size) {
                baseMonths.add(
                    nStr1[i] + "月"
                )
            }
            if (leapMonth(year) != 0) {
                baseMonths.add(
                        leapMonth(year),
                    "闰" + nStr1[leapMonth(year)] + "月"
                )
            }
            return baseMonths
        }

        /**
         * 获取每月农历显示名称
         *
         * @param maxDay 天
         * @return 名称列表
         */
        fun getLunarDays(maxDay: Int): ArrayList<String> {
            val days = ArrayList<String>()
            for (i in 1..maxDay) {
                days.add(getChinaDate(i))
            }
            return days
        }
    }
}