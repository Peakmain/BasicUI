package com.peakmain.basicui.activity.home

import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.widget.AutoDeleteEditText

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
class AutoEditTextActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_auto_edit_text
    }

    override fun initView() {
        mNavigationBuilder!!.setTitleText("AutoEditText的使用")
                .create()
         findViewById<AutoDeleteEditText>(R.id.auto_edit_text).setText("你好，我是Peakmain")
    }

    override fun initData() {}
}