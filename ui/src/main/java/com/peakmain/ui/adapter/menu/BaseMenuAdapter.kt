package com.peakmain.ui.adapter.menu

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.peakmain.ui.adapter.BaseAdapter

/**
 * author ：Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：多条目菜单筛选的适配器
 */
abstract class BaseMenuAdapter : BaseAdapter() {
    //订阅用户放到这个集合
    private var mObserver: MenuObserver? = null

    /**
     * 订阅注册
     */
    fun registerDataSetObserver(observer: MenuObserver?) {
        mObserver = observer
    }

    /**
     * 解注册
     */
    fun unregisterDataSetObserver() {
        mObserver = null
    }

    fun closeMenu() {
        if (mObserver != null) {
            mObserver!!.closeMenu()
        }
    }

    /**
     * 获取Menu的view
     */
    abstract fun getMenuView(position: Int, parent: ViewGroup): View

    /**
     * 打开菜单
     */
    abstract fun openMenu(menuTabView: LinearLayout?, tabView: View)

    /**
     * 关闭菜单
     * @param menuTabView 所有的Tab View
     * @param tabView 当前 tabView
     * @param position 当前tabView的position
     * @param isSwitch 是否是点击切换
     */
    abstract fun closeMenu(menuTabView: LinearLayout, tabView: View, position: Int, isSwitch: Boolean)

    /**
     * 阴影点击事件
     */
    open fun shadowClick(): Boolean {
        return false
    }

}