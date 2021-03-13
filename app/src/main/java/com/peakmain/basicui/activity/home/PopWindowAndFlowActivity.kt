package com.peakmain.basicui.activity.home

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.peakmain.basicui.R
import com.peakmain.basicui.adapter.FlowLayoutAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.widget.CustomPopupWindow
import com.peakmain.ui.widget.FlowLayout
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
class PopWindowAndFlowActivity : BaseActivity() {
    private var mFlowLayout: FlowLayout? = null
    private var mButton: Button? = null
    private var mAdapter: FlowLayoutAdapter? = null
    private lateinit var mList: MutableList<String>
    override fun getLayoutId(): Int {
        return R.layout.activity_pop_window_flow
    }

    override fun initView() {
        mButton = findViewById(R.id.bt_test)
        mFlowLayout = findViewById(R.id.flow_layout)
        mNavigationBuilder
                ?.setTitleText("popwindow和flowlayout的使用")
                ?.create()
    }

    override fun initData() {
        mList = ArrayList()
        for (i in 8..49) {
            mList.add("自定" + i + "标签")
        }
        showButton()
        showFlowLayout()
    }

    private fun showFlowLayout() {
        mAdapter = FlowLayoutAdapter(this, mList)
        mFlowLayout!!.setAdapter(mAdapter)
    }

    private fun showButton() {
        val textView = TextView(this)
        textView.setText(R.string.app_name)
        val customPopupWindow = CustomPopupWindow.PopupWindowBuilder(this)
                .setView(textView)
                .setBgDarkAlpha(0.7f)
                .create()
        mButton!!.setOnClickListener { anchor: View? -> customPopupWindow.showAsDropDown(anchor) }
    }
}