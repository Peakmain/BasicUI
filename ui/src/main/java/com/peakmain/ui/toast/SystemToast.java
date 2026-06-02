package com.peakmain.ui.toast;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * @author ：Peakmain
 * createTime：2026/6/2
 * mail:2726449200@qq.com
 * describe：
 */
public class SystemToast implements IToast {
    private final Toast mToast;

    private final Context mContext;

    public void makeTextShow(String text, long duration) {
        new SystemToast(mContext)
                .setText(text)
                .setDuration(duration).show();
    }

    public static IToast makeText(Context context, String text, long duration) {
        return new CustomToast(context)
                .setText(text)
                .setDuration(duration);
    }

    public SystemToast(Context context) {
        mContext = context;
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    @Override
    public IToast setGravity(int gravity, int xOffset, int yOffset) {
        mToast.setGravity(gravity, xOffset, yOffset);
        return this;
    }

    @Override
    public IToast setDuration(long durationMillis) {
        mToast.setDuration((int) durationMillis);
        return this;
    }

    /**
     * 不能和{@link #setText(String)}一起使用，要么{@link #setView(View)} 要么{@link #setView(View)}
     *
     * @param view 传入view
     * @return 自身对象
     */
    @Override
    public IToast setView(View view) {
        mToast.setView(view);
        return this;
    }

    @Override
    public IToast setMargin(float horizontalMargin, float verticalMargin) {
        mToast.setMargin(horizontalMargin, verticalMargin);
        return this;
    }

    /**
     * 不能和{@link #setView(View)}一起使用，要么{@link #setView(View)} 要么{@link #setView(View)}
     *
     * @param text 传入字符串
     * @return 自身对象
     */
    @Override
    public IToast setText(String text) {
        mToast.setText(text);
        return this;
    }

    @Override
    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

    @Override
    public void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}