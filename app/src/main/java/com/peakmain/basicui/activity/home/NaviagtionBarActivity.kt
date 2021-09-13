package com.peakmain.basicui.activity.home

import android.view.View
import android.view.ViewGroup
import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.basicui.view.RadioCancelButton
import com.peakmain.ui.navigationbar.DefaultNavigationBar
import com.peakmain.ui.widget.ShapeTextView

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
class NaviagtionBarActivity : BaseActivity() {
    /**
     * 显示右边箭头
     */
    private var mRadioShowRightArrow: RadioCancelButton? = null

    /**
     * 设置左边文字为"\"返回\""并设置颜色为white
     */
    private var mRadioSetLeftText: RadioCancelButton? = null

    /**
     * 隐藏左边文字
     */
    private var mRadioHideLeftText: RadioCancelButton? = null

    /**
     * 隐藏标题
     */
    private var mRadioHideTitle: RadioCancelButton? = null

    /**
     * 修改背景颜色
     */
    private var mRadioAlertBackgroundColor: RadioCancelButton? = null

    /**
     * 显示返回按钮
     */
    private var mRadioShowBackButton: RadioCancelButton? = null

    /**
     * 显示toolbar中默认自带的title
     */
    private var mRadioShowToolbarTitle: RadioCancelButton? = null

    /**
     * 修改右边图片资源
     */
    private var mRadioAlertRightImage: RadioCancelButton? = null

    /**
     * 修改左边返回图片资源
     */
    private var mRadioAlerLeftIcon: RadioCancelButton? = null

    /**
     * 确定
     */
    private lateinit var mStvSure: ShapeTextView
    private var mBuilder: DefaultNavigationBar.Builder? = null
    private var mNavigationBar: DefaultNavigationBar? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_navigation_bar
    }

    override fun initView() {
        mRadioShowRightArrow = findViewById(R.id.radio_show_right_arrow)
        mRadioSetLeftText = findViewById(R.id.radio_set_left_text)
        mRadioHideLeftText = findViewById(R.id.radio_hide_left_text)
        mRadioHideTitle = findViewById(R.id.radio_hide_title)
        mRadioAlertBackgroundColor = findViewById(R.id.radio_alert_background_color)
        mRadioShowBackButton = findViewById(R.id.radio_show_back_button)
        mRadioShowToolbarTitle = findViewById(R.id.radio_show_toolbar_title)
        mRadioAlertRightImage = findViewById(R.id.radio_alert_right_image)
        mRadioAlerLeftIcon = findViewById(R.id.radio_alert_left_back_icon)
        mStvSure = findViewById(R.id.stv_sure)
        mStvSure.setOnClickListener { showNavigationBar() }
    }

    /**
     * 显示navigationbar
     */
    private fun showNavigationBar() {
        val radioShowRightArrowChecked = mRadioShowRightArrow!!.isChecked
        val radioSetLeftTextChecked = mRadioSetLeftText!!.isChecked
        val radioHideLeftTextChecked = mRadioHideLeftText!!.isChecked
        val radioHideTitleChecked = mRadioHideTitle!!.isChecked
        val radioAlertBackgroundColorChecked = mRadioAlertBackgroundColor!!.isChecked
        val radioShowBackButtonChecked = mRadioShowBackButton!!.isChecked
        val radioShowToolbarTitleChecked = mRadioShowToolbarTitle!!.isChecked
        val radioAlertRightImageChecked = mRadioAlertRightImage!!.isChecked
        val radioAlertLeftIconChecked = mRadioAlerLeftIcon!!.isChecked

        if (mNavigationBar != null) {
            val parent = mRadioShowRightArrow!!.parent as ViewGroup
            parent.removeViewAt(0)
        }
        mBuilder = DefaultNavigationBar.Builder(this, findViewById(R.id.view_root))
                .setLeftText("左边")
        if (!radioShowRightArrowChecked) {
            mBuilder!!.hideRightView()
        }
        if (radioHideLeftTextChecked) {
            mBuilder!!.hideLeftText()
        } else if (radioSetLeftTextChecked) {
            mBuilder!!.setLeftText("返回")
        }
        if (radioHideTitleChecked) {
            mBuilder!!.hideTitleText()
        } else {
            mBuilder!!.setTitleText("navigationBar的使用")
        }
        if (radioAlertBackgroundColorChecked) {
            mBuilder!!.setToolbarBackgroundColor(R.color.ui_color_802F73F6)
        }
        if(radioAlertLeftIconChecked){
            mBuilder!!.setNavigationIcon(R.drawable.ui_ic_left_back)
        }
        mBuilder!!.setDisplayHomeAsUpEnabled(radioShowBackButtonChecked)
                .setNavigationOnClickListener(View.OnClickListener { v: View? -> finish() })
        mBuilder!!.setDisplayShowTitleEnabled(radioShowToolbarTitleChecked)
        if (radioAlertRightImageChecked) {
            mBuilder!!.setRightResId(R.drawable.ic_et_close)
        }
        mNavigationBar = mBuilder!!.create()
    }

    override fun initData() {}
}