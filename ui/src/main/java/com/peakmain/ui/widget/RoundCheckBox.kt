package com.peakmain.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.support.v7.appcompat.R
import android.support.v7.widget.AppCompatCheckBox
import android.util.AttributeSet

/**
 * author ：Peakmain
 * createTime：2020/3/27
 * mail:2726449200@qq.com
 * describe：
 */
class RoundCheckBox @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.radioButtonStyle) : AppCompatCheckBox(context, attrs, defStyleAttr) {
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val checked = isChecked
        buttonTintList = if (!checked) {
            ContextCompat.getColorStateList(context, com.peakmain.ui.R.color.color_6A6A6A)
        } else {
            ContextCompat.getColorStateList(context, com.peakmain.ui.R.color.color_08B906)
        }
    }
}