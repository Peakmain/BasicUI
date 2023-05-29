package com.peakmain.ui.navigationbar

import android.graphics.Typeface
import android.view.View

/**
 * author ：Peakmain
 * createTime：2020-01-10
 * mail:2726449200@qq.com
 * describe：存放参数
 */
class NavigationParameter {
    //控件的文本
    var text: CharSequence? = null

    //控件的点击事件
    var clickListener: View.OnClickListener? = null

    //文字颜色id的集合
    var textColorId = 0
    var typeface :Typeface= Typeface.DEFAULT
}