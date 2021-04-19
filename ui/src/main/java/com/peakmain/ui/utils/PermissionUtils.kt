package com.peakmain.ui.utils

import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * author ：Peakmain
 * createTime：2021/4/19
 * mail:2726449200@qq.com
 * describe：权限工具类
 */
typealias PermissionCallback = (Boolean, List<String>) -> Unit

object PermissionUtils {
    private const val TAG = "PermissionFragment"
    fun request(
            activity: FragmentActivity,
            vararg permissions: String,
            callback: PermissionCallback
    ) {
        val fragmentManager = activity.supportFragmentManager
        val findFragmentByTag = fragmentManager.findFragmentByTag(TAG)
        val fragment = if (findFragmentByTag != null) {
            findFragmentByTag as PermissionFragment
        } else {
            val fragment = PermissionFragment()
            fragmentManager.beginTransaction().add(fragment, TAG).commitNow()
            fragment
        }
        fragment.requestNow(callback, *permissions)
    }
}

internal class PermissionFragment : Fragment() {
    var callback: PermissionCallback? = null
    fun requestNow(callback: PermissionCallback, vararg permissions: String) {
        this.callback = callback
        requestPermissions(permissions, 12)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 12) {
            val deniedList = ArrayList<String>()
            for ((index, result) in grantResults.withIndex()) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    deniedList.add(permissions[index])
                }
            }
            val allGranted = deniedList.isEmpty()
            callback?.let {
                it(allGranted, deniedList)
            }
        }
    }
}