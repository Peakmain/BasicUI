package com.peakmain.ui.wheelview.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.peakmain.ui.R
import com.peakmain.ui.dialog.AlertDialog
import com.peakmain.ui.utils.SizeUtils
import com.peakmain.ui.wheelview.adapter.ArrayWheelAdapter

/**
 * author ：Peakmain
 * createTime：2021/5/17
 * mail:2726449200@qq.com
 * describe：基本WheelView
 */
abstract class BaseWheelView<T>(private val mContext: Context, private val mViewType: ViewType) :
        View.OnClickListener {
    //ONE 展示一个  TWO 两个 ALL 所有
    enum class ViewType {
        ONE, TWO, ALL
    }

    private var mDialog: AlertDialog? = null
    private lateinit var mView: View
    private var tvTitle: TextView? = null
    private lateinit var mTvCancel: TextView
    private lateinit var mTvConfirm: TextView
    lateinit var mWheelView1: WheelView
    lateinit var mWheelView2: WheelView
    lateinit var mWheelView3: WheelView
    private var mTextSize = 0
    private var mUnSelectTextColor = 0
    private var mSelectTextColor = 0
    private var mDividerColor = 0

    /**
     * 初始化View
     */
    private fun initView() {
        val inflater =
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = inflater.inflate(R.layout.ui_item_base_wheelview, null)
        mWheelView1 = mView.findViewById(R.id.ui_wheel_view1)
        mWheelView2 = mView.findViewById(R.id.ui_wheel_view2)
        mWheelView3 = mView.findViewById(R.id.ui_wheel_view3)
        tvTitle = mView.findViewById(R.id.ui_tv_title)
        mTvCancel = mView.findViewById(R.id.ui_tv_cancel)
        mTvConfirm = mView.findViewById(R.id.ui_tv_confirm)
        mTvConfirm.setOnClickListener(this)
        mTvCancel.setOnClickListener(this)
        if (mViewType == ViewType.ONE) {
            mWheelView1.visibility = View.VISIBLE
        } else if (mViewType == ViewType.TWO) {
            mWheelView1.visibility = View.VISIBLE
            mWheelView2.visibility = View.VISIBLE
        } else if (mViewType == ViewType.ALL) {
            mWheelView1.visibility = View.VISIBLE
            mWheelView2.visibility = View.VISIBLE
            mWheelView3.visibility = View.VISIBLE
        }
        intDefaultOption()
    }

    /**
     * 创建Dialog
     */
    private fun createDialog() {
        mDialog = AlertDialog.Builder(mContext)
                .setContentView(mView)
                .setCancelable(true)
                .fromButtom(true)
                .setFullWidth()
                .create()
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.ui_tv_cancel) {
            cancel()
        } else if (id == R.id.ui_tv_confirm) {
            confirm()
        }
    }

    /**
     * 初始化默认配置
     */
    private fun intDefaultOption() {
        mTextSize = 18
        mSelectTextColor =
                ContextCompat.getColor(mContext, R.color.ui_color_4A4A4A)
        mUnSelectTextColor =
                ContextCompat.getColor(mContext, R.color.ui_color_AFAFAF)
        mDividerColor = ContextCompat.getColor(mContext, R.color.ui_color_F5F5F5)
        setTextSize(mTextSize)
        setSelectTextColor(mSelectTextColor)
        setUnSelectTextColor(mUnSelectTextColor)
        setDividerColor(mDividerColor)
        setDividerType(WheelView.DividerType.FILL)
        setCyclic(false)
        setGravity(Gravity.CENTER)
        isCenterLabel(false)
    }

    /**
     * 设置标题
     *
     * @param title
     */
    fun setTitle(title: String?): BaseWheelView<T> {
        if (!TextUtils.isEmpty(title)) {
            tvTitle!!.text = title
        }
        return this
    }

    /**
     * 设置字体大小
     */
    fun setTextSize(mTextSize: Int): BaseWheelView<T> {
        mWheelView1.setTextSize(mTextSize.toFloat())
        mWheelView2.setTextSize(mTextSize.toFloat())
        mWheelView3.setTextSize(mTextSize.toFloat())
        return this
    }

    /**
     * 设置未选中的字体颜色
     *
     * @param colorOut
     */
    fun setUnSelectTextColor(colorOut: Int): BaseWheelView<T> {
        mWheelView1.setUnSelectTextColor(colorOut)
        mWheelView2.setUnSelectTextColor(colorOut)
        mWheelView3.setUnSelectTextColor(colorOut)
        return this
    }

    /**
     * 设置选中的字体颜色
     */
    fun setSelectTextColor(color: Int): BaseWheelView<T> {
        mWheelView1.setSelectTextColor(color)
        mWheelView2.setSelectTextColor(color)
        mWheelView3.setSelectTextColor(color)
        return this
    }

    /**
     * 设置Itemde线条颜色
     */
    fun setDividerColor(color: Int): BaseWheelView<T> {
        mWheelView1.setDividerColor(color)
        mWheelView2.setDividerColor(color)
        mWheelView3.setDividerColor(color)
        return this
    }

    /**
     * 设置Item线条样式
     */
    fun setDividerType(dividerType: WheelView.DividerType?): BaseWheelView<T> {
        mWheelView1.setDividerType(dividerType)
        mWheelView2.setDividerType(dividerType)
        mWheelView3.setDividerType(dividerType)
        return this
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic 是否循环
     */
    fun setCyclic(cyclic: Boolean): BaseWheelView<T> {
        mWheelView1.setCyclic(cyclic)
        mWheelView2.setCyclic(cyclic)
        mWheelView3.setCyclic(cyclic)
        return this
    }

    /**
     * 设置位置
     *
     * @param gravity
     */
    fun setGravity(gravity: Int): BaseWheelView<T> {
        mWheelView1.setGravity(gravity)
        mWheelView2.setGravity(gravity)
        mWheelView3.setGravity(gravity)
        return this
    }

    /**
     * 是否只显示中间选中项的label文字，false则每项item全部都带有label。
     */
    fun isCenterLabel(isCenterLabel: Boolean): BaseWheelView<T> {
        mWheelView1.isCenterLabel(isCenterLabel)
        mWheelView2.isCenterLabel(isCenterLabel)
        mWheelView3.isCenterLabel(isCenterLabel)
        return this
    }

    /**
     * 设置设配器
     *
     * @param items
     */
    fun setAdapter1(items: List<T?>?, index: Int) {
        mWheelView1.adapter = ArrayWheelAdapter<Any?>(items)
        mWheelView1.currentItem = index
    }

    fun setAdapter2(items: List<T?>?, index: Int) {
        mWheelView2.adapter = ArrayWheelAdapter<Any?>(items)
        mWheelView2.currentItem = index
    }

    fun setAdapter3(items: List<T?>?, index: Int) {
        mWheelView3.adapter = ArrayWheelAdapter<Any?>(items)
        mWheelView3.currentItem = index
    }

    /**
     * 获取选中结果
     */
    val result1: String
        get() = mWheelView1.adapter.getItem(mWheelView1.currentItem).toString()

    val result2: String
        get() = mWheelView2.adapter.getItem(mWheelView2.currentItem).toString()

    val result3: String
        get() = mWheelView3.adapter.getItem(mWheelView3.currentItem).toString()

    /**
     * 显示弹窗
     */
    fun show() {
        if (mDialog != null && !mDialog!!.isShowing) {
            mDialog!!.show()
        }
    }

    /**
     * 关闭弹窗
     */
    fun dismiss() {
        if (mDialog != null && mDialog!!.isShowing) {
            mDialog!!.dismiss()
        }
    }

    /**
     * 初始化
     */
    abstract fun init()

    /**
     * 取消
     */
    abstract fun cancel()

    /**
     * 确认
     */
    abstract fun confirm()

    init {
        initView()
        createDialog()
        init()
    }
}