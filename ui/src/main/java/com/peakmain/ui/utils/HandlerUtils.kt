package com.peakmain.ui.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * author ：Peakmain
 * createTime：2020/9/12
 * mail:2726449200@qq.com
 * describe：
 */
object HandlerUtils {
    private val HANDLER = Handler(Looper.getMainLooper())
    private var sCurProcessName: String? = null
    @JvmStatic
    fun isMainProcess(context: Context): Boolean {
        val processName = getCurProcessName(context)
        return if (processName != null && processName.contains(":")) {
            false
        } else processName != null && processName == context.packageName
    }

    //判断是否是主进程
    val isOnMainThread: Boolean
        get() = Looper.myLooper() == Looper.getMainLooper()

    fun getCurProcessName(context: Context): String? {
        val procName = sCurProcessName
        if (!TextUtils.isEmpty(procName)) {
            return procName
        }
        try {
            val pid = Process.myPid()
            val mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (appProcess in mActivityManager.runningAppProcesses) {
                if (appProcess.pid == pid) {
                    sCurProcessName = appProcess.processName
                    return sCurProcessName
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        sCurProcessName = curProcessNameFromProc
        return sCurProcessName
    }// ignore

    // ignore
    private val curProcessNameFromProc: String?
        private get() {
            var cmdlineReader: BufferedReader? = null
            try {
                cmdlineReader = BufferedReader(InputStreamReader(
                        FileInputStream(
                                "/proc/" + Process.myPid() + "/cmdline"),
                        "iso-8859-1"))
                var c: Int
                val processName = StringBuilder()
                while (cmdlineReader.read().also { c = it } > 0) {
                    processName.append(c.toChar())
                }
                return processName.toString()
            } catch (e: Throwable) {
                // ignore
            } finally {
                if (cmdlineReader != null) {
                    try {
                        cmdlineReader.close()
                    } catch (e: Exception) {
                        // ignore
                    }
                }
            }
            return null
        }

    /**
     * Run on ui thread
     *
     * @param runnable
     */
    @JvmStatic
    fun runOnUiThread(runnable: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
        } else {
            HANDLER.post(runnable)
        }
    }

    /**
     * Run on ui thread delay
     *
     * @param runnable
     * @param delayMillis
     */
    @JvmStatic
    fun runOnUiThreadDelay(runnable: Runnable?, delayMillis: Long) {
        HANDLER.postDelayed(runnable, delayMillis)
    }

    /**
     * Remove runnable
     *
     * @param runnable
     */
    fun removeRunable(runnable: Runnable?) {
        HANDLER.removeCallbacks(runnable)
    }
}