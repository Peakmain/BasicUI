package com.peakmain.ui.adapter.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.peakmain.ui.R

/**
 * author ：Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：基本条目的适配器
 */
abstract class BaseListMenuAdapter(
    private val mContext: Context,//上下文
    private val mTitles: List<String> //标题的集合
) : BaseMenuAdapter() {

    override fun getView(position: Int, parent: ViewGroup?): View? {
        val view = LayoutInflater.from(mContext).inflate(titleLayoutId, parent, false)
        val textView = view.findViewById<TextView>(R.id.tv_menu_tab_title)
        textView.text = mTitles[position]
        setTitleContent(textView, position)
        return view
    }


    override val count: Int
        get() = mTitles.size

    override fun getMenuView(position: Int, parent: ViewGroup): View {
        val view = LayoutInflater.from(mContext).inflate(getMenuLayoutId(position), parent, false)
        setMenuContent(view, position)
        return view
    }


    override fun openMenu(tabView: View) {
        val textView = tabView.findViewById<TextView>(R.id.tv_menu_tab_title)
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.ui_color_6CBD9B))
    }

    override fun closeMenu(tabView: View) {
        val textView = tabView.findViewById<TextView>(R.id.tv_menu_tab_title)
        textView.setTextColor(
            ContextCompat.getColor(
                mContext,
                R.color.ui_color_272A2B
            )
        )
    }

    open fun setTitleContent(textView: TextView?, position: Int) {

    }

    protected abstract val titleLayoutId: Int
    protected abstract  val contentViewId: Int
    protected abstract fun getMenuLayoutId(position: Int): Int
    protected abstract fun setMenuContent(menuView: View?, position: Int)
}