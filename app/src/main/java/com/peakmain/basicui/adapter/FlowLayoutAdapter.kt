package com.peakmain.basicui.adapter

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.peakmain.basicui.R
import com.peakmain.ui.adapter.flow.BaseFlowAdapter
import com.peakmain.ui.utils.ToastUtils
import com.peakmain.ui.widget.CustomPopupWindow

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
class FlowLayoutAdapter(private val mContext: Context, private val mData: List<String>) : BaseFlowAdapter() {
    override val count: Int
        get() = mData.size

    override fun getView(position: Int, parent: ViewGroup?): View? {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_flow_layout, parent, false)
        val tagTv = view.findViewById<TextView>(R.id.tv_label)
        val llLocation: ConstraintLayout = view.findViewById(R.id.ll_location)
        tagTv.text = mData[position]
        tagTv.setOnClickListener(object : View.OnClickListener {
            private var mCustomPopupWindow: CustomPopupWindow? = null
            override fun onClick(v: View) {
                tagTv.setTextColor(ContextCompat.getColor(mContext, android.R.color.white))
                tagTv.background = ContextCompat.getDrawable(mContext, R.drawable.select_bg)
                mCustomPopupWindow = CustomPopupWindow.PopupWindowBuilder(mContext)
                        .setView(R.layout.item_label_popwindow_view)
                        .setBgDarkAlpha(0.7f)
                        .setOnDissmissListener(PopupWindow.OnDismissListener {

                            //取消选中
                            tagTv.setTextColor(Color.parseColor("#ffffff"))
                            tagTv.background = ContextCompat.getDrawable(mContext, R.drawable.check_bg)
                        })
                        .create()
                val location = IntArray(2)
                tagTv.getLocationOnScreen(location)
                mCustomPopupWindow!!.showAtLocation(llLocation, Gravity.NO_GRAVITY, location[0] + tagTv.width / 2 - mCustomPopupWindow!!.width / 2, location[1] - tagTv.measuredHeight - tagTv.paddingTop * 2)
                if (mCustomPopupWindow != null) {
                    mCustomPopupWindow!!.getView<View>(R.id.tv_delete).setOnClickListener { v1: View? ->
                        ToastUtils.showShort("删除成功")
                        mCustomPopupWindow!!.dissmiss()
                    }
                }
            }
        })
        return view
    }

}