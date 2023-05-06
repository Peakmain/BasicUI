package com.peakmain.ui.navigationbar

import android.content.Context
import android.view.ViewGroup

/**
 * author ：Peakmain
 * createTime：2018/11/26
 * mail:2726449200@qq.com
 * describe：
 */
class NavigationBar internal constructor(builder: Builder?) :
    AbsNavigationBar<NavigationBar.Builder?>(builder) {
    class Builder(context: Context?, layoutId: Int, parent: ViewGroup?) :
        AbsNavigationBar.Builder<Builder?>(context!!, layoutId, parent!!) {
        override fun create(): NavigationBar {
            return NavigationBar(this)
        }
    }
}