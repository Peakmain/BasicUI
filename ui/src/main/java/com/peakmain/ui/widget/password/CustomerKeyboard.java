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

/**
 * author ：Peakmain
 * createTime：2020/3/12
 * mail:2726449200@qq.com
 * describe：
 */
public class CustomerKeyboard extends LinearLayout implements View.OnClickListener {
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
            if(v.getId()==R.id.textView4){
                return;
            }
            if (mListener != null) {
                mListener.click(number);
            }
        }
        if(v instanceof ImageView){
            if(mListener!=null){
                mListener.delete();
            }
        }
    }
    // 设置点击回掉监听
    private CustomerKeyboardClickListener mListener;

    public void setOnCustomerKeyboardClickListener(CustomerKeyboardClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 点击键盘的回调监听
     */
    public interface CustomerKeyboardClickListener {
        public void click(String number);

        public void delete();
    }
}
