package com.peakmain.ui.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.peakmain.ui.constants.BasicUIUtils
import java.util.*

/**
 * author ：Peakmain
 * createTime：2021/4/21
 * mail:2726449200@qq.com
 * describe：权限封装工具类
 */
class PermissionUtils private constructor(var mObject: Any) {

    companion object {
        private var requestCode: Int = -1
        private var mOnPermissionListener: OnPermissionListener? = null
        /**
         * 判断是否有某个权限
         */
        fun hasPermission(permission: String): Boolean {
            return try {
                ContextCompat.checkSelfPermission(
                        BasicUIUtils.application!!,
                        permission
                ) == PackageManager.PERMISSION_GRANTED
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        fun request(activity: Activity, requestCode: Int, permissions: Array<String>, block: OnPermissionListener) {
            this.requestCode=requestCode
            with(activity).requestCode(requestCode).requestPermission(*permissions)
                    .request(block)
        }

        fun request(fragment: Fragment, requestCode: Int, permissions: Array<String>, block: OnPermissionListener) {
            this.requestCode=requestCode
            with(fragment).requestCode(requestCode).requestPermission(*permissions)
                    .request(block)
        }

        private fun with(activity: Activity): PermissionUtils {
            return PermissionUtils(activity)
        }

        private fun with(fragment: Fragment): PermissionUtils {
            return PermissionUtils(fragment)
        }

        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>) {
            if (this.requestCode != -1 && this.requestCode == requestCode) {
                val deniedPermissions = getDeniedPermissions(*permissions)
                if (deniedPermissions.isEmpty()) {
                    mOnPermissionListener?.onPermissionGranted()
                } else {
                    mOnPermissionListener?.onPermissionDenied(deniedPermissions)
                }
            }
        }

        /**
         * 获取请求权限中需要授权的权限
         */
        private fun getDeniedPermissions(vararg permissions: String): List<String> {
            val deniedPermissions = ArrayList<String>()
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                                BasicUIUtils.application!!,
                                permission
                        ) == PackageManager.PERMISSION_DENIED
                ) {
                    deniedPermissions.add(permission)
                }
            }
            return deniedPermissions
        }
    }

    private var mRequestCode: Int = -1
    private lateinit var mPermission: Array<out String>
    private fun requestCode(requestCode: Int): PermissionUtils {
        this.mRequestCode = requestCode
        return this
    }

    fun getRequestCode(): Int {
        return mRequestCode
    }

    private fun requestPermission(vararg permissions: String): PermissionUtils {
        this.mPermission = permissions
        return this
    }

    /**
     * 判断是否大于6.0
     */
    fun isOVerMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private fun request(block: OnPermissionListener) {
        mOnPermissionListener = block
        val deniedPermissions = getDeniedPermissions(*mPermission)
        if (!isOVerMarshmallow() || deniedPermissions.isEmpty()) {
            //如果小于6.0并且已经有申请过权限
            block.onPermissionGranted()
        } else {
            //大于6.0并且权限没有申请过
            ActivityCompat.requestPermissions(getActivity(mObject)!!
                    , deniedPermissions.toTypedArray()
                    , mRequestCode);
        }
    }


    private fun getActivity(context: Any): Activity? {
        if (context is Activity) {
            return context
        } else if (context is Fragment) {
            return context.activity!!
        }
        return null
    }


    interface OnPermissionListener {

        fun onPermissionGranted()

        fun onPermissionDenied(deniedPermissions: List<String>)
    }
}