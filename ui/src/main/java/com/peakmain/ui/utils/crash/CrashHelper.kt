package com.peakmain.ui.utils.crash

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.Process
import android.os.StatFs
import android.text.format.Formatter
import com.peakmain.ui.BuildConfig
import com.peakmain.ui.constants.BasicUIUtils
import com.peakmain.ui.utils.ActivityUtils
import com.peakmain.ui.utils.LogUtils
import java.io.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log
import kotlin.system.exitProcess

/**
 * author ：Peakmain
 * createTime：2021/5/7
 * mail:2726449200@qq.com
 * describe：
 */
internal object CrashHelper {
    var CRASH_DIR = "crash_dir"
    fun init(crashDir: String) {
        Thread.setDefaultUncaughtExceptionHandler(CaughtExceptionHandler())
        this.CRASH_DIR = crashDir
    }

    class CaughtExceptionHandler : Thread.UncaughtExceptionHandler {
        private val context = BasicUIUtils.application!!
        private val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA)
        private val LAUNCH_TIME = formatter.format(Date())
        private val mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        override fun uncaughtException(thread: Thread?, e: Throwable?) {
            if (!handleException(e) && mDefaultExceptionHandler != null) {
                //默认系统处理
                mDefaultExceptionHandler.uncaughtException(thread, e)
            }
            //重启app
            restartApp()
        }

        private fun restartApp() {
            val intent: Intent? =
                    context.packageManager?.getLaunchIntentForPackage(context.packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)

            Process.killProcess(Process.myPid())
            exitProcess(10)
        }

        //是否有异常
        private fun handleException(e: Throwable?): Boolean {
            if (e == null) return false
            val logInfo = createLogInfo(e)
            if (BuildConfig.DEBUG) {
                LogUtils.e(logInfo)
            }
            val file = saveCrashInfoToFile(logInfo)
            if(CrashUtils.mListener!=null){
                CrashUtils.mListener!!.onFileUploadListener(file)
            }
            return true
        }

        private fun saveCrashInfoToFile(logInfo: String): File {
            val crashDir = File(CRASH_DIR)
            if (!crashDir.exists()) {
                crashDir.mkdirs()
            }
            val file = File(crashDir, formatter.format(Date()) + "-crash.txt")
            file.createNewFile()
            LogUtils.e(file.absolutePath)
            val fos = FileOutputStream(file)
            try {
                fos.write(logInfo.toByteArray())
                fos.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                fos.close()
            }
            return file
        }

        private fun createLogInfo(e: Throwable): String {
            val sb = StringBuilder()
            sb.append("brand=${Build.BRAND}\n")
            sb.append("rom=${Build.MODEL}\n")
            sb.append("os=${Build.VERSION.RELEASE}\n")
            sb.append("sdk=${Build.VERSION.SDK_INT}\n")
            sb.append("launch_time=${LAUNCH_TIME}\n")//启动app的时间
            sb.append("crash_time=${formatter.format(Date())}\n")//crash发生时间
            sb.append("forground=${ActivityUtils.mInstance.isFront}\n")//应用处于前后台
            sb.append("thread=${Thread.currentThread().name}\n")//异常线程名
            sb.append("cpu_arch=${Build.CPU_ABI}\n")

            //app 信息
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            sb.append("versionCode=${packageInfo.versionCode}\n")//版本号
            sb.append("versionName=${packageInfo.versionName}\n")
            sb.append("packageName=${packageInfo.packageName}\n")
            sb.append("requestedPermission=${Arrays.toString(packageInfo.requestedPermissions)}\n")

            val memoryInfo = ActivityManager.MemoryInfo()
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.getMemoryInfo(memoryInfo)
            sb.append("availMemory=${Formatter.formatFileSize(context, memoryInfo.availMem)}\n")//可用内存
            sb.append("totalMemory=${Formatter.formatFileSize(context, memoryInfo.totalMem)}\n")//设备总内存

            val file = Environment.getExternalStorageDirectory()
            //手机内部可用空间
            val statFs = StatFs(file.path)
            val availableSize = statFs.availableBlocks * statFs.blockSize
            sb.append(
                    "availStorage=${Formatter.formatFileSize(
                            context,
                            availableSize.toLong()
                    )}\n"
            )
            val write: Writer = StringWriter()
            val printWriter = PrintWriter(write)
            e.printStackTrace(printWriter)
            var cause = e.cause
            while (cause != null) {
                cause.printStackTrace(printWriter)
                cause = cause.cause
            }
            printWriter.close()
            sb.append(write.toString())
            return sb.toString()
        }

    }
}


