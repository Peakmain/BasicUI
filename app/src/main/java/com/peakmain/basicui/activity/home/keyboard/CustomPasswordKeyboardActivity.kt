package com.peakmain.basicui.activity.home.keyboard

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.utils.ToastUtils
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
        mCustomerKeyboard!!.setOnCustomerKeyboardClickListener(object :
            SimpleCustomKeyboardListener() {
            override fun click(number: String?) {
                mEditText!!.addPasswordNumber(number)
            }

            override fun delete() {
                mEditText!!.deletePassWord()
            }
        })
        mEditText!!.setPasswordCompleteListener { text: String -> ToastUtils.showShort(text) }
        val tvShowPassword = findViewById<TextView>(R.id.tv_show_password)
        tvShowPassword?.setOnClickListener {
            if (mEditText?.isPasswordVisible == true) {
                mEditText?.isPasswordVisible = false
                tvShowPassword.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(
                            this@CustomPasswordKeyboardActivity,
                            R.drawable.ic_password_hide
                        ), null, null, null
                    )
                    text="隐藏密码"
                }
            } else {
                mEditText?.isPasswordVisible = true
                tvShowPassword.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(
                            this@CustomPasswordKeyboardActivity,
                            R.drawable.ic_password_show
                        ), null, null, null
                    )
                    text="显示密码"

                }

            }
        }
    }
}