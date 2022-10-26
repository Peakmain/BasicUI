package com.peakmain.ui.utils

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import com.peakmain.ui.constants.BasicUIUtils
import java.lang.ref.WeakReference

/**
 * author ：Peakmain
 * createTime：2021/5/6
 * mail:2726449200@qq.com
 * describe：
 */
class ActivityUtils {

    private val mActivityLists = ArrayList<WeakReference<Activity>>()

    //前台后台回调
    private val mFrontBackCallbacks = ArrayList<FrontBackCallback>()

    //上一个阶段是否是前台
    var isFront = true
    private var mActivityStartCount = 0//>0表示应用在前台
    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks())
    }

    /**
     * 获得当前栈顶没有被销毁的activity
     */
    fun getTopActivity(isAlive: Boolean): Activity? {
        if (mActivityLists.size <= 0) {
            return null
        } else {
            val activityRef = mActivityLists[mActivityLists.size - 1]
            val activity = activityRef.get()
            if (isAlive) {
                if (activity == null || activity.isFinishing || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed)) {
                    mActivityLists.remove(activityRef)
                    return getTopActivity(isAlive)
                }
            }
            return activity
        }
    }

    inner class ActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(p0: Activity) {

        }

        override fun onActivityResumed(p0: Activity) {
        }

        override fun onActivityStarted(p0: Activity) {
            mActivityStartCount++
            //!isFront表示之前处于后台
            if (!isFront && mActivityStartCount > 0) {
                isFront = true
                onFrontBackChanged(isFront)
            }
        }

        override fun onActivityDestroyed(p0: Activity) {
            for (activityRef in mActivityLists) {
                if (activityRef.get() == p0) {
                    mActivityLists.remove(activityRef)
                    break
                }
            }

        }


        override fun onActivityStopped(p0: Activity) {
            mActivityStartCount--
            if (mActivityStartCount <= 0 && isFront) {
                isFront = false
                onFrontBackChanged(isFront)
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityCreated(p0: Activity, p1: Bundle?) {
            mActivityLists.add(WeakReference(p0))
        }

    }

    private fun onFrontBackChanged(front: Boolean) {
        for (callback in mFrontBackCallbacks) {
            callback.onChanged(front)
        }
    }

    fun addFrontBackCallback(callback: FrontBackCallback) {
        if (!mFrontBackCallbacks.contains(callback)) {
            mFrontBackCallbacks.add(callback)
        }
    }

    fun removeFrontBackCallback(callback: FrontBackCallback) {
        mFrontBackCallbacks.remove(callback)
    }

    interface FrontBackCallback {
        fun onChanged(front: Boolean)
    }

    companion object {
        private val mApplication = BasicUIUtils.application!!

        /**
         * 跳转到悬浮窗设置界面
         */
        fun startOverlaySettingActivity() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mApplication.startActivity(Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + mApplication.packageName)
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }

        @JvmStatic
        val mInstance: ActivityUtils by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityUtils()
        }
    }
}