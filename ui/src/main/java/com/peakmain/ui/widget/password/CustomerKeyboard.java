package com.peakmain.ui.widget.password;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peakmain.ui.R;
import com.peakmain.ui.widget.listener.SimpleCustomKeyboardListener;

/**
 * author ：Peakmain
 * createTime：2020/3/12
 * mail:2726449200@qq.com
 * describe：普通键盘和身份证键盘
 */
public class CustomerKeyboard extends LinearLayout implements View.OnClickListener {
    //是否显示身份证信息
    public boolean isExtraKey = true;

    public CustomerKeyboard(Context context) {
        this(context, null);
    }


    public CustomerKeyboard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerKeyboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.ui_pay_password_keyboard, this);
        setItemClickListener(this);
        findViewById(R.id.tv_custom_keyboard_x).setVisibility(isExtraKey ? VISIBLE : GONE);
        findViewById(R.id.iv_keyboard_hide).setVisibility(isExtraKey ? GONE : VISIBLE);
    }

    /**
     * 设置子view的点击事件
     */
    private void setItemClickListener(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;//LinearLayout
            //继续遍历
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                //不断遍历循环
                View childView = viewGroup.getChildAt(i);
                setItemClickListener(childView);
            }
        } else {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            String number = ((TextView) v).getText().toString().trim();
            if (!isExtraKey && v.getId() == R.id.tv_custom_keyboard_x) {
                return;
            }
            if (mListener != null) {
                mListener.click(number);
            }
        } else if (v instanceof ImageView) {
            if (v.getId() == R.id.iv_keyboard_delete) {
                if (mListener != null) {
                    mListener.delete();
                }
            } else if (v.getId() == R.id.iv_keyboard_hide) {
                if (mListener != null)
                    mListener.dissmiss();
            }
        }
    }

    // 设置点击回掉监听
    private SimpleCustomKeyboardListener mListener;

    public void setOnCustomerKeyboardClickListener(SimpleCustomKeyboardListener listener) {
        this.mListener = listener;
    }

    public boolean isExtraKey() {
        return isExtraKey;
    }
}
