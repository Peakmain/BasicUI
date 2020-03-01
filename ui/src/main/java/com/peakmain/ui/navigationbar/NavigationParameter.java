package com.peakmain.ui.navigationbar;

import android.view.View;

/**
 * author: peakmain
 * version: 1.0
 * createTime: 2020-01-10 18:01
 * mail:2726449200@qq.com
 * description: 存放一些参数的类
 */
public class NavigationParameter {
    //控件的文本
    private CharSequence text;
    //控件的点击事件
    private View.OnClickListener clickListener;
   //文字颜色id的集合
    private int textColorId;

    public CharSequence getText() {
        return text;
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public int getTextColorId() {
        return textColorId;
    }

    public void setTextColorId(int textColorId) {
        this.textColorId = textColorId;
    }
}
