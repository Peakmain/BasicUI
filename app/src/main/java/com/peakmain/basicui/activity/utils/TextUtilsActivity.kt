package com.peakmain.basicui.activity.utils

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.navigationbar.DefaultNavigationBar
import com.peakmain.ui.utils.OnClickableSpan
import com.peakmain.ui.utils.TextUtils
import com.peakmain.ui.utils.ToastUtils

/**
 * author ：Peakmain
 * createTime：2021/4/28
 * mail:2726449200@qq.com
 * describe：文本高亮工具类
 */
class TextUtilsActivity : BaseActivity() {
    private lateinit var mTvContent1: TextView
    private lateinit var mTvContent2: TextView
    override fun getLayoutId(): Int {
        return R.layout.activity_text_utils
    }

    override fun initView() {
        mTvContent1 = findViewById(R.id.tv_content1)
        mTvContent2 = findViewById(R.id.tv_content2)
        mNavigationBuilder?.setTitleText("文本高亮工具类")?.create()
    }

    override fun initData() {
        //第一种方式加载——无点击事件
        mTvContent1.text = TextUtils.clipTextColor(mTvContent1.text.toString(), ContextCompat.getColor(this, R.color.ui_color_01a8e3), mTvContent1.text.indexOf("加"), mTvContent1.length())

        TextUtils.Builder(mTvContent2)
                //第一个高亮的文本区域
                .setClipText(ContextCompat.getColor(this, R.color.ui_color_01a8e3), mTvContent2.text.indexOf("加"), 7)
                .setOnClickableSpan(object : OnClickableSpan {
                    override fun onClickableSpan(view: View) {
                        ToastUtils.showShort("点击了“加载”")
                    }

                })
                .setStyleSpan(Typeface.BOLD_ITALIC)
                .showUnderLine(true)
                //第二个高亮文本区域
                .setClipText(ContextCompat.getColor(this, R.color.ui_color_01a8e3), mTvContent2.text.indexOf("点"), mTvContent2.length())
                .setAbsoluteTextSize(28)
                .setBackgroundColor(ContextCompat.getColor(this, R.color.ui_color_CDCECE))
                .setOnClickableSpan(object : OnClickableSpan {
                    override fun onClickableSpan(view: View) {
                        ToastUtils.showShort("点击了“点击”")
                    }
                })
                .create()

    }

}