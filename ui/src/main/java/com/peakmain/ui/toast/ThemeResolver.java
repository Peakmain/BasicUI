package com.peakmain.ui.toast;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.AttrRes;
/**
 * @author ：Peakmain
 * createTime：2026/6/2
 * mail:2726449200@qq.com
 * describe：
 */
class ThemeResolver {

    public static int getColor(Context context, @AttrRes int attr) {
        return getColor(context, attr, 0);
    }

    public static int getColor(Context context, @AttrRes int attr, int defaultColor) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getColor(0, defaultColor);
        } finally {
            a.recycle();
        }
    }

    public static int getDimen(Context context, @AttrRes int attr, int defaultSize) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getDimensionPixelSize(0, defaultSize);
        } finally {
            a.recycle();
        }
    }

}
