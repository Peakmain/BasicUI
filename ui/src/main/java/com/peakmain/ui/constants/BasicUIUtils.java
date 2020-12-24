package com.peakmain.ui.constants;

import android.app.Application;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.peakmain.ui.BuildConfig;

/**
 * author ：Peakmain
 * createTime：2020/3/16
 * mail:2726449200@qq.com
 * describe：
 */
public class BasicUIUtils {
    private static Application mApplication;

    /**
     * 获取布局的view
     *
     * @param context  context
     * @param layoutId 布局的id
     * @return view
     */
    public static View getView(Context context, @LayoutRes int layoutId) {
        if (context == null) {
            throw new NullPointerException("context cannot be null");
        }
        LayoutInflater inflater = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(layoutId, null);
    }

    /**
     * 获取全局上下文
     */
    public static Application getApplication() {
        if (mApplication != null)
            return mApplication;
        //通过反射获取上下文
        init(ReflectUtils.getApplicationByReflect());
        return mApplication;
    }

    public static void init(Application application) {
        if (application == null) {
            Log.e(BuildConfig.TAG, "application is null.");
            return;
        }
        if (!application.equals(mApplication)) {
            mApplication = application;
        }
    }


}
