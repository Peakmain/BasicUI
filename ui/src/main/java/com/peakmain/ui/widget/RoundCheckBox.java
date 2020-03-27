package com.peakmain.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import com.peakmain.ui.R;

/**
 * author ：Peakmain
 * createTime：2020/3/27
 * mail:2726449200@qq.com
 * describe：
 */
public class RoundCheckBox extends AppCompatCheckBox {
    public RoundCheckBox(Context context) {
        this(context,null);
    }

    public RoundCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.radioButtonStyle);
    }


    public RoundCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean checked = isChecked();
        if(!checked){
            setButtonTintList(ContextCompat.getColorStateList(getContext(), R.color.color_6A6A6A));
        }else{
            setButtonTintList(ContextCompat.getColorStateList(getContext(),R.color.color_08B906));
        }
    }
}
