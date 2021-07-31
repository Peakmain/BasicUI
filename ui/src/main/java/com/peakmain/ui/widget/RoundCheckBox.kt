package com.peakmain.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import com.peakmain.ui.R

/**
 * author ：Peakmain
 * createTime：2020/3/27
 * mail:2726449200@qq.com
 * describe：
 */
class RoundCheckBox @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.radioButtonStyle) : AppCompatCheckBox(context, attrs, defStyleAttr) {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val checked = isChecked
        buttonTintList = if (!checked) {
            ContextCompat.getColorStateList(context, com.peakmain.ui.R.color.ui_color_6A6A6A)
        } else {
            ContextCompat.getColorStateList(context, com.peakmain.ui.R.color.ui_color_08B906)
        }
    }
}