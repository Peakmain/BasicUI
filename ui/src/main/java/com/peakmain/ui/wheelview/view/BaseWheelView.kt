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

    private var mDialog: Dialog? = null
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
        mDialog = Dialog(mContext)
        mDialog!!.setContentView(mView)
        mDialog!!.setCancelable(true)
        mDialog!!.setCanceledOnTouchOutside(true)
        // 定义Dialog布局和参数
        val dialogWindow = mDialog!!.window
        dialogWindow!!.decorView.setPadding(0, 0, 0, 0)
        val lp = dialogWindow.attributes
        dialogWindow.setGravity(Gravity.LEFT or Gravity.BOTTOM)
        lp.x = 0
        lp.y = 0
        lp.height = SizeUtils.screenHeight / 5 * 2
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.windowAnimations = R.style.dialog
        dialogWindow.decorView.setBackgroundColor(Color.TRANSPARENT)
        dialogWindow.attributes = lp
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
            ContextCompat.getColor(mContext, R.color.color_4A4A4A)
        mUnSelectTextColor =
            ContextCompat.getColor(mContext, R.color.color_AFAFAF)
        mDividerColor = ContextCompat.getColor(mContext, R.color.color_F5F5F5)
        setmTextSize(mTextSize)
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
    protected fun setTitle(title: String?) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle!!.text = title
        }
    }

    /**
     * 设置字体大小
     */
    protected fun setmTextSize(mTextSize: Int) {
        mWheelView1.setTextSize(mTextSize.toFloat())
        mWheelView2.setTextSize(mTextSize.toFloat())
        mWheelView3.setTextSize(mTextSize.toFloat())
    }

    /**
     * 设置未选中的字体颜色
     *
     * @param colorOut
     */
    protected fun setUnSelectTextColor(colorOut: Int) {
        mWheelView1.setUnSelectTextColor(colorOut)
        mWheelView2.setUnSelectTextColor(colorOut)
        mWheelView3.setUnSelectTextColor(colorOut)
    }

    /**
     * 设置选中的字体颜色
     */
    protected fun setSelectTextColor(color: Int) {
        mWheelView1.setSelectTextColor(color)
        mWheelView2.setSelectTextColor(color)
        mWheelView3.setSelectTextColor(color)
    }

    /**
     * 设置Itemde线条颜色
     */
    protected fun setDividerColor(color: Int) {
        mWheelView1.setDividerColor(color)
        mWheelView2.setDividerColor(color)
        mWheelView3.setDividerColor(color)
    }

    /**
     * 设置Item线条样式
     */
    protected fun setDividerType(dividerType: WheelView.DividerType?) {
        mWheelView1.setDividerType(dividerType)
        mWheelView2.setDividerType(dividerType)
        mWheelView3.setDividerType(dividerType)
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic 是否循环
     */
    protected fun setCyclic(cyclic: Boolean) {
        mWheelView1.setCyclic(cyclic)
        mWheelView2.setCyclic(cyclic)
        mWheelView3.setCyclic(cyclic)
    }

    /**
     * 设置位置
     *
     * @param gravity
     */
    protected fun setGravity(gravity: Int) {
        mWheelView1.setGravity(gravity)
        mWheelView2.setGravity(gravity)
        mWheelView3.setGravity(gravity)
    }

    /**
     * 是否只显示中间选中项的label文字，false则每项item全部都带有label。
     */
    protected fun isCenterLabel(isCenterLabel: Boolean) {
        mWheelView1.isCenterLabel(isCenterLabel)
        mWheelView2.isCenterLabel(isCenterLabel)
        mWheelView3.isCenterLabel(isCenterLabel)
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