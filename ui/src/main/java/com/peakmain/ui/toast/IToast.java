package com.peakmain.ui.toast;

import android.view.View;

/**
 * @author ：Peakmain
 * createTime：2026/6/2
 * mail:2726449200@qq.com
 * describe：
 */
public interface IToast {
    void makeTextShow(String text, long duration);

    IToast setGravity(int gravity, int xOffset, int yOffset);

    IToast setDuration(long durationMillis);

    /**
     * 不能和{@link #setText(String)}一起使用，要么{@link #setView(View)} 要么{@link #setText(String)}
     */
    IToast setView(View view);

    IToast setMargin(float horizontalMargin, float verticalMargin);

    /**
     * 不能和{@link #setView(View)}一起使用，要么{@link #setView(View)} 要么{@link #setText(String)}
     */
    IToast setText(String text);

    void show();

    void cancel();
}