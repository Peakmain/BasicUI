package com.peakmain.ui.wheelview.view

import android.content.Context
import com.peakmain.ui.utils.wheel.WheelTimeUtil
import java.util.*

/**
 * author ：Peakmain
 * createTime：2021/5/17
 * mail:2726449200@qq.com
 * describe：时间选择器 年月日
 */
class TimePickerWheelView(
    mContext: Context,
    private val mCallback: OnSelectTimeCallback?
) : BaseWheelView<Any?>(mContext, ViewType.ALL) {
    private var mWheelTimeUtil: WheelTimeUtil? = null
    override fun init() {
        mWheelTimeUtil = WheelTimeUtil(mWheelView1, mWheelView2, mWheelView3)
        mWheelTimeUtil!!.setLabel("年", "月", "日")
        setDefaultTime()
    }

    /**
     * 设置默认时间
     */
    private fun setDefaultTime() {
        val year: Int
        val month: Int
        val day: Int
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH]
        day = calendar[Calendar.DAY_OF_MONTH]
        mWheelTimeUtil!!.setPicker(year, month, day)
    }

    override fun cancel() {
        dismiss()
    }

    override fun confirm() {
        mCallback?.onSelectTime(mWheelTimeUtil!!.time)
        dismiss()
    }

    interface OnSelectTimeCallback {
        fun onSelectTime(time: String?)
    }

}