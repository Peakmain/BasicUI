package com.peakmain.basicui.activity.home.keyboard;

import android.text.InputType;
import android.widget.EditText;

import com.peakmain.basicui.R;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.ui.dialog.AlertDialog;
import com.peakmain.ui.widget.listener.SimpleCustomKeyboardListener;
import com.peakmain.ui.widget.password.CustomerKeyboard;

/**
 * author ：Peakmain
 * createTime：2020/3/12
 * mail:2726449200@qq.com
 * describe：自定义小数点键盘
 */
public class CustomPointKeyboardActivity extends BaseActivity {
    private CustomerKeyboard mCustomerKeyboard;
    private EditText mEditText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_keyboard_point;
    }

    @Override
    protected void initView() {
        mCustomerKeyboard = findViewById(R.id.custom_key_board);
        mEditText = findViewById(R.id.password_edit_text);
        mNavigationBuilder.setTitleText("自定义小数点键盘").create();

    }

    @Override
    protected void initData() {
        mEditText.setInputType(InputType.TYPE_NULL);
       mEditText.setOnClickListener((v) -> showDialog());

    }

    private void showDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .fromButtom(true)
                .setContentView(R.layout.dialog_custom_keyboard_point)
                .setFullWidth()
                .show();
        mCustomerKeyboard = dialog.findViewById(R.id.custom_key_board);
        mCustomerKeyboard.setOnCustomerKeyboardClickListener(new SimpleCustomKeyboardListener() {
            @Override
            public void click(String number) {
                String text = mEditText.getText().toString();
                mEditText.setText(text + number);
            }

            @Override
            public void delete() {
                String passWord = mEditText.getText().toString().trim();
                if (passWord.length() <= 0) {
                    return;
                }
                passWord = passWord.substring(0, passWord.length() - 1);
                mEditText.setText(passWord);
            }
        });
    }
}
