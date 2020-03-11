package com.peakmain.basicui.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * author ：Peakmain
 * createTime：2020/3/10
 * mail:2726449200@qq.com
 * describe：
 */
public class RadioCancelButton extends android.support.v7.widget.AppCompatRadioButton {
    public RadioCancelButton(Context context) {
        super(context);
    }

    public RadioCancelButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioCancelButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }
}
