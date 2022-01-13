package com.peakmain.ui.constants

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import com.peakmain.ui.BuildConfig

/**
 * author ：Peakmain
 * createTime：2020/3/16
 * mail:2726449200@qq.com
 * describe：
 */
object BasicUIUtils {
    private var mApplication: Application? = null

    /**
     * 获取布局的view
     *
     * @param context  context
     * @param layoutId 布局的id
     * @return view
     */
    @JvmStatic
    fun getView(context: Context?, @LayoutRes layoutId: Int): View {
        if (context == null) {
            throw NullPointerException("context cannot be null")
        }
        val inflater = context.applicationContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return inflater.inflate(layoutId, null)
    }//通过反射获取上下文

    /**
     * 获取全局上下文
     */
    @JvmStatic
    val application: Application?
        get() {
            if (mApplication != null) return mApplication
            //通过反射获取上下文
            init(ReflectUtils.applicationByReflect)
            return mApplication
        }

    fun init(application: Application?) {
        if (application == null) {
            Log.e(BuildConfig.TAG, "application is null.")
            return
        }
        if (application != mApplication) {
            mApplication = application
        }
    }
}