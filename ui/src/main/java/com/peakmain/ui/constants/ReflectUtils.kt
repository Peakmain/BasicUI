package com.peakmain.ui.constants

import android.app.Application
import com.peakmain.ui.constants.BasicUIUtils.application

/**
 * author ：Peakmain
 * createTime：2020/12/24
 * mail:2726449200@qq.com
 * describe：
 */
internal object ReflectUtils {
    //类
    val applicationByReflect: Application?
        get() {
            try {
                //类
                val activityThreadClass = Class.forName("android.app.ActivityThread")
                //activityThread对象获取
                val activityThreadStaticField = activityThread
                val application = activityThreadClass.getMethod("getApplication")
                    .invoke(activityThreadStaticField)
                    ?: return null
                return application as Application
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

    private val activityThread: Any?
        get() {
            var `object` = activityThreadStaticField
            if (`object` != null) return `object`
            `object` = activityThreadByStaticMethod
            return `object` ?: activityThreadByLoadedApkField
        }

    private val activityThreadByLoadedApkField: Any?
        get() = try {
            val mLoadedApkField = Application::class.java.getDeclaredField("mLoadedApk")
            mLoadedApkField.isAccessible = true
            val mLoadedApk = mLoadedApkField[application]
            val mActivityThread = mLoadedApk.javaClass.getDeclaredField("mActivityThread")
            mActivityThread.isAccessible = true
            mActivityThread[mLoadedApk]
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    private val activityThreadByStaticMethod: Any?
        get() = try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            activityThreadClass.getMethod("currentActivityThread").invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    //类
    private val activityThreadStaticField: Any?
        get() =//类
            try {
                val activityThreadClass = Class.forName("android.app.ActivityThread")
                val sCurrentActivityThread =
                    activityThreadClass.getDeclaredField("sCurrentActivityThread")
                sCurrentActivityThread.isAccessible = true
                sCurrentActivityThread[null]
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
}