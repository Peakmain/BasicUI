package com.peakmain.ui.navigationbar

import android.view.View

/**
 * author: peakmain
 * version: 1.0
 * createTime: 2020-01-10 18:01
 * mail:2726449200@qq.com
 * description: 存放一些参数的类
 */
class NavigationParameter {
    //控件的文本
    var text: CharSequence? = null

    //控件的点击事件
    var clickListener: View.OnClickListener? = null

    //文字颜色id的集合
    var textColorId = 0

}