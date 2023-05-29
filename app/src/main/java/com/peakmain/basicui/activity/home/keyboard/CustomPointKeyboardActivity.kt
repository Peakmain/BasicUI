package com.peakmain.basicui.activity.home.keyboard

import android.text.InputType
import android.view.View
import android.widget.EditText
import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.dialog.AlertDialog
import com.peakmain.ui.widget.listener.SimpleCustomKeyboardListener
import com.peakmain.ui.widget.password.CustomerKeyboard

/**
 * author ：Peakmain
 * createTime：2020/3/12
 * mail:2726449200@qq.com
 * describe：自定义小数点键盘
 */
class CustomPointKeyboardActivity : BaseActivity() {
    private var mCustomerKeyboard: CustomerKeyboard? = null
    private var mEditText: EditText? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_custom_keyboard_point
    }

    override fun initView() {
        mCustomerKeyboard = findViewById(R.id.custom_key_board)
        mEditText = findViewById(R.id.password_edit_text)
        mNavigationBuilder!!.setTitleText("自定义小数点键盘").create()
    }

    override fun initData() {
        mEditText!!.inputType = InputType.TYPE_NULL
        mEditText!!.setOnClickListener { v: View? -> showDialog() }
    }

    private fun showDialog() {
        val dialog = AlertDialog.Builder(this)
                .fromBottom(true)
                .setContentView(R.layout.dialog_custom_keyboard_point)
                .setFullWidth()
                .show()
        mCustomerKeyboard = dialog.findViewById(R.id.custom_key_board)
        mCustomerKeyboard?.setOnCustomerKeyboardClickListener(object : SimpleCustomKeyboardListener() {
            override fun click(number: String?) {
                val text = mEditText!!.text.toString()
                mEditText!!.setText(text + number)
            }

            override fun delete() {
                var passWord = mEditText!!.text.toString().trim { it <= ' ' }
                if (passWord.length <= 0) {
                    return
                }
                passWord = passWord.substring(0, passWord.length - 1)
                mEditText!!.setText(passWord)
            }
        })
    }
}