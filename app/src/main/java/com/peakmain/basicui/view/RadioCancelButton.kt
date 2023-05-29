package com.peakmain.basicui.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.R
import androidx.appcompat.widget.AppCompatRadioButton

/**
 * author ：Peakmain
 * createTime：2020/3/10
 * mail:2726449200@qq.com
 * describe：
 */
class RadioCancelButton @JvmOverloads constructor(context: Context?, attrs: AttributeSet?=null, defStyleAttr: Int= R.attr.radioButtonStyle) :
    AppCompatRadioButton(context,attrs, defStyleAttr){
    override fun toggle() {
        isChecked = !isChecked
    }
}