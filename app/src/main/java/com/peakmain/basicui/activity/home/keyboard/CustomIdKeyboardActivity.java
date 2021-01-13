package com.peakmain.basicui.activity.home.keyboard;

import com.peakmain.basicui.R;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.basicui.utils.ToastUtils;
import com.peakmain.ui.widget.listener.SimpleCustomKeyboardListener;
import com.peakmain.ui.widget.password.CustomerKeyboard;
import com.peakmain.ui.widget.password.PasswordEditText;

/**
 * author ：Peakmain
 * createTime：2020/3/12
 * mail:2726449200@qq.com
 * describe：自定义身份证键盘
 */
public class CustomIdKeyboardActivity extends BaseActivity {
    private CustomerKeyboard mCustomerKeyboard;
    private PasswordEditText mEditText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_id;
    }

    @Override
    protected void initView() {
        mCustomerKeyboard = findViewById(R.id.custom_key_board);
        mEditText = findViewById(R.id.password_edit_text);
        mNavigationBuilder.setTitleText("自定义身份证键盘").create();

    }

    @Override
    protected void initData() {
        mCustomerKeyboard.setOnCustomerKeyboardClickListener(new SimpleCustomKeyboardListener() {
            @Override
            public void click(String number) {
                mEditText.addPasswordNumber(number);
            }

            @Override
            public void delete() {
                 mEditText.deletePassWord();
            }
        });
        mEditText.setPasswordCompleteListener(ToastUtils::showShort);
    }
}
