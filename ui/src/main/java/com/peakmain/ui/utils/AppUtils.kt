package com.peakmain.ui.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.peakmain.ui.constants.BasicUIUtils
import java.io.File

/**
 * author ：Peakmain
 * createTime：2021/4/21
 * mail:2726449200@qq.com
 * describe：
 */
object AppUtils {
    fun installApp(filePath: String?) {
        installApp(FileUtils.getFileByPath(filePath))
    }

    fun installApp(file: File?) {
        if (!FileUtils.isFileExists(file)) return
        BasicUIUtils.application!!.startActivity(getInstallAppIntent(file!!, true))
    }

    fun installApp(activity: Activity,
                   filePath: String?,
                   requestCode: Int) {
        installApp(activity, FileUtils.getFileByPath(filePath), requestCode)
    }

    fun installApp(activity: Activity,
                   file: File?,
                   requestCode: Int) {
        if (!FileUtils.isFileExists(file)) return
        activity.startActivityForResult(getInstallAppIntent(file!!), requestCode)
    }

    private fun getInstallAppIntent(file: File): Intent? {
        return getInstallAppIntent(file, false)
    }

    private fun getInstallAppIntent(file: File, isNewTask: Boolean): Intent? {

        val intent = Intent(Intent.ACTION_VIEW)
        val data: Uri
        val type = "application/vnd.android.package-archive"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file)
        } else {
            val authority: String = BasicUIUtils.application!!.packageName.toString() + ".provider"
            data = FileProvider.getUriForFile(BasicUIUtils.application!!, authority, file)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        BasicUIUtils.application!!.grantUriPermission(BasicUIUtils.application?.packageName, data, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(data, type)
        return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
    }

}