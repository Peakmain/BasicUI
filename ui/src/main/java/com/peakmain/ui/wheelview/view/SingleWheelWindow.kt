package com.peakmain.ui.wheelview.view

import android.content.Context
import com.peakmain.ui.wheelview.adapter.ArrayWheelAdapter

/**
 * author:Peakmain
 * createTime:2021/5/17
 * mail:2726449200@qq.com
 * describe：通用单个滚轮
 */
class SingleWheelWindow<T>(
    context: Context,
    private val mCallback: OnSelectOptionCallback<T>?
) : BaseWheelView<T>(context, ViewType.ONE) {
    override fun init() {}

    /**
     * 设置选项集合
     */
    fun setWheelOptions(options: List<T?>?) {
        mWheelView1.adapter = ArrayWheelAdapter(options)
        mWheelView1.currentItem = 0
    }

    override fun cancel() {
        dismiss()
    }

    override fun confirm() {
        if (mCallback != null) {
            val value = mWheelView1.adapter.getItem(mWheelView1.currentItem) as T
            mCallback.onSelectOption(value, mWheelView1.currentItem)
        }
        dismiss()
    }

    interface OnSelectOptionCallback<T> {
        fun onSelectOption(option: T, position: Int)
    }

}