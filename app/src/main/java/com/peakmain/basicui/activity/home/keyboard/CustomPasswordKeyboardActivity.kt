package com.peakmain.basicui.activity.home.keyboard

import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.basicui.utils.ToastUtils
import com.peakmain.ui.widget.listener.SimpleCustomKeyboardListener
import com.peakmain.ui.widget.password.CustomerKeyboard
import com.peakmain.ui.widget.password.PasswordEditText

/**
 * author ：Peakmain
 * createTime：2020/3/12
 * mail:2726449200@qq.com
 * describe：
 */
class CustomPasswordKeyboardActivity : BaseActivity() {
    private var mCustomerKeyboard: CustomerKeyboard? = null
    private var mEditText: PasswordEditText? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_custom_password
    }

    override fun initView() {
        mCustomerKeyboard = findViewById(R.id.custom_key_board)
        mEditText = findViewById(R.id.password_edit_text)
        mNavigationBuilder!!.setTitleText("自定义键盘").create()
    }

    override fun initData() {
        mCustomerKeyboard!!.setOnCustomerKeyboardClickListener(object : SimpleCustomKeyboardListener() {
            override fun click(number: String?) {
                mEditText!!.addPasswordNumber(number)
            }

            override fun delete() {
                mEditText!!.deletePassWord()
            }
        })
        mEditText!!.setPasswordCompleteListener { text: String -> ToastUtils.showShort(text) }
    }
}