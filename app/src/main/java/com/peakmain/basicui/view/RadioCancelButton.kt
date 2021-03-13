package com.peakmain.basicui.view

import android.content.Context
import android.support.v7.widget.AppCompatRadioButton
import android.util.AttributeSet

/**
 * author ：Peakmain
 * createTime：2020/3/10
 * mail:2726449200@qq.com
 * describe：
 */
class RadioCancelButton : AppCompatRadioButton {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun toggle() {
        isChecked = !isChecked
    }
}