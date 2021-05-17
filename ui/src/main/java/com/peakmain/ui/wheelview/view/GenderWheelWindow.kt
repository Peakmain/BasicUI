package com.peakmain.ui.wheelview.view

import android.content.Context
import com.peakmain.ui.wheelview.adapter.ArrayWheelAdapter

/**
 * author ：Peakmain
 * createTime：2021/5/17
 * mail:2726449200@qq.com
 * describe：性别滚轮选择
 */
class GenderWheelWindow(context: Context, private val mCallback: OnSelectGenderCallback) :
    BaseWheelView<String>(context, ViewType.ONE) {

    override fun init() {
        val items = ArrayList<String>()
        items.add("男")
        items.add("女")
        mWheelView1.adapter = ArrayWheelAdapter(items)
        mWheelView1.currentItem = 0
    }

    override fun cancel() {
        dismiss()
    }

    override fun confirm() {
        val item = mWheelView1.adapter.getItem(mWheelView1.currentItem) as String
        mCallback.onSelectGenderCallback(item)
        dismiss()
    }

    interface OnSelectGenderCallback {
        fun onSelectGenderCallback(option: String)
    }

}