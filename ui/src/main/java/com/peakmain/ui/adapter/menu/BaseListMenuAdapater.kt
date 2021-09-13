package com.peakmain.ui.adapter.menu

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.peakmain.ui.R

/**
 * author ：Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：基本条目的适配器
 */
abstract class BaseListMenuAdapater(//上下文
        private val mContext: Context, //标题的集合
        private val mTitles: List<String>) : BaseMenuAdapter() {

    override fun getView(position: Int, parent: ViewGroup?): View? {
        val view = LayoutInflater.from(mContext).inflate(titleLayoutId, parent, false)
        val textView = view.findViewById<TextView>(R.id.tv_menu_tab_title)
        textView.text = mTitles[position]
        return view
    }

    abstract val titleLayoutId: Int
    override val count: Int
        get() = mTitles.size

    override fun getMenuView(position: Int, parent: ViewGroup): View {
        val view = LayoutInflater.from(mContext).inflate(menuLayoutId, parent, false)
        setMenuContent(view, position)
        return view
    }

    protected abstract fun setMenuContent(menuView: View?, position: Int)
    protected abstract val menuLayoutId: Int
    override fun openMenu(tabView: View) {
        val textView = tabView.findViewById<TextView>(R.id.tv_menu_tab_title)
        textView.setTextColor(Color.parseColor("#01a8e3"))
    }

    override fun closeMenu(tabView: View) {
        val textView = tabView.findViewById<TextView>(R.id.tv_menu_tab_title)
        textView.setTextColor(Color.BLACK)
    }

}