package com.peakmain.ui.widget.password;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
    public String isExtraKey = "";
    //小数点后多少位
    private int decimalPlaces = 100;

    //是否点击了小数点
    private boolean isSelectDecimalPoint = false;
    private int mDecimalPointCount;

    public CustomerKeyboard(Context context) {
        this(context, null);
    }


    public CustomerKeyboard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerKeyboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.ui_custom_keyboard, this);
        setItemClickListener(this);
        TextView tvCustomKeyboard = findViewById(R.id.tv_custom_keyboard_x);
        tvCustomKeyboard.setVisibility(!TextUtils.isEmpty(isExtraKey) ? VISIBLE : GONE);
        tvCustomKeyboard.setText(isExtraKey);
        findViewById(R.id.iv_keyboard_hide).setVisibility(!TextUtils.isEmpty(isExtraKey) ? GONE : VISIBLE);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.customKeyboardView);
        isExtraKey = ta.getString(R.styleable.customKeyboardView_ckExtraKey);
        decimalPlaces = ta.getInt(R.styleable.customKeyboardView_ckDecimalPlaces, decimalPlaces);
        ta.recycle();
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
            if (".".equals(number)) {
                if (!isSelectDecimalPoint) {
                    if (mListener != null) {
                        mListener.click(number);
                    }
                    isSelectDecimalPoint = true;
                }
            } else {
                if (isSelectDecimalPoint) {
                    if (mDecimalPointCount < decimalPlaces) {
                        //点了小数点
                        mDecimalPointCount++;
                        if (mListener != null) {
                            mListener.click(number);
                        }
                    } else {
                        return;
                    }
                } else{
                    if (mListener != null) {
                        mListener.click(number);
                    }
                }
            }

        } else if (v instanceof ImageView) {
            if (v.getId() == R.id.iv_keyboard_delete) {
                if (isSelectDecimalPoint) {
                    if (mDecimalPointCount > 0) {
                        mDecimalPointCount--;
                    } else {
                        mDecimalPointCount = 0;
                        isSelectDecimalPoint = false;
                    }
                }
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

    public String isExtraKey() {
        return isExtraKey;
    }
}
