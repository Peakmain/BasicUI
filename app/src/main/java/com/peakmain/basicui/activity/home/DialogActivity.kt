package com.peakmain.basicui.activity.home

import android.util.Log
import android.view.View
import com.peakmain.basicui.BuildConfig
import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.basicui.view.RadioCancelButton
import com.peakmain.ui.dialog.AlertDialog
import com.peakmain.ui.utils.SizeUtils
import com.peakmain.ui.utils.ToastUtils
import com.peakmain.ui.widget.ShapeTextView

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
class DialogActivity : BaseActivity(), View.OnClickListener {
    /**
     * 是否设置宽度全屏
     */
    private var mRadioSetWidthFull: RadioCancelButton? = null

    /**
     * 是否添加默认动画
     */
    private var mRadioSetDefaultAnimation: RadioCancelButton? = null

    /**
     * 是否从底部弹出
     */
    private var mRadioSetFromBottom: RadioCancelButton? = null

    /**
     * 点击外部是否可以取消
     */
    private var mRadioSetCanCancel: RadioCancelButton? = null

    /**
     * 确定
     */
    private lateinit var mStvSure: ShapeTextView
    override fun getLayoutId(): Int {
        return R.layout.activity_dialog_demo
    }

    public override fun initView() {
        mRadioSetWidthFull = findViewById(R.id.radio_set_width_full)
        mRadioSetDefaultAnimation = findViewById(R.id.radio_set_default_animation)
        mRadioSetFromBottom = findViewById(R.id.radio_set_from_bottom)
        mRadioSetCanCancel = findViewById(R.id.radio_set_can_cancel)
        mStvSure = findViewById(R.id.stv_sure)
        mStvSure.setOnClickListener(this)
        //添加NavigationBar
        mNavigationBuilder!!.setTitleText("dialog的使用")
            .create()
    }

    override fun initData() {}
    override fun onClick(v: View) {
        when (v.id) {
            R.id.stv_sure -> showDialog()
            else -> {
            }
        }
    }

    private fun showDialog() {
        val radioSetWidthFullChecked = mRadioSetWidthFull!!.isChecked
        val radioSetDefaultAnimationChecked = mRadioSetDefaultAnimation!!.isChecked
        val radioSetFromBottomChecked = mRadioSetFromBottom!!.isChecked
        val radioSetCanCancelChecked = mRadioSetCanCancel!!.isChecked
        Log.e(
            BuildConfig.TAG,
            radioSetWidthFullChecked.toString() + "," + radioSetDefaultAnimationChecked + ","
                    + radioSetFromBottomChecked + "," + radioSetCanCancelChecked
        )
        val builder = AlertDialog.Builder(this)
            .setContentView(R.layout.dialog_show)
            .setText(R.id.tv_text,
                "中国中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客中国第七十降级的计算机视觉是是是几点到几点觉得二级额监控软件恶客")
            .setMaxHeight(SizeUtils.screenHeight*0.8)
            .setOnClickListener(
                R.id.tv_save_image
            ) { ToastUtils.showShort("保存图片成功") }
            .setOnClickListener(
                R.id.tv_forword
            ) { ToastUtils.showShort("转发") }
        builder.setCancelable(radioSetCanCancelChecked)
        if (radioSetWidthFullChecked) {
            builder.setFullWidth()
        }
        if (radioSetDefaultAnimationChecked) {
            builder.addDefaultAnimation()
        }
        if (radioSetFromBottomChecked) {
            builder.fromBottom(radioSetFromBottomChecked)
        }
        val dialog = builder.create()
        dialog.show()
        dialog.setOnClickListener(R.id.tv_cancel, View.OnClickListener { dialog.dismiss() })
    }
}