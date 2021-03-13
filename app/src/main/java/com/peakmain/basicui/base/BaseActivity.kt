package com.peakmain.basicui.base

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.peakmain.basicui.R
import com.peakmain.basicui.utils.ToastUtils
import com.peakmain.ui.navigationbar.DefaultNavigationBar
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
abstract class BaseActivity : AppCompatActivity(), PermissionCallbacks {
    @JvmField
    var mNavigationBuilder: DefaultNavigationBar.Builder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutId())
        mNavigationBuilder = DefaultNavigationBar.Builder(this, findViewById(android.R.id.content))
                .hideLeftText()
                .setDisplayHomeAsUpEnabled(true)
                .setNavigationOnClickListener(View.OnClickListener { v: View? -> finish() })
                .hideRightView()
                .setToolbarBackgroundColor(R.color.colorAccent)
        initView()
        initData()
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorStatus)
                .fitsSystemWindows(true)
                .statusBarAlpha(0.3f) //状态栏透明度，不写默认0.0f
                .navigationBarAlpha(0.4f) //导航栏透明度，不写默认0.0F
                .barAlpha(0.3f) //状态栏和导航栏透明度，不写默认0.0f
                .statusBarDarkFont(false) //状态栏字体是深色，不写默认为亮色
                .navigationBarDarkIcon(false) //导航栏图标是深色，不写默认为亮色
                .init()
    }

    protected abstract fun getLayoutId(): Int
    protected abstract fun initView()
    protected abstract fun initData()

    /**
     * 1、获取是否有网络权限
     */
    fun haveNetWorkPerm(): Boolean {
        val perms = arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        )
        return EasyPermissions.hasPermissions(this, *perms)
    }

    /**
     * 申请网络权限
     */
    @AfterPermissionGranted(RC_NETWORK)
    fun requestNetWorkPerm() {
        val perms = arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            ToastUtils.showShort("网络权限已经申请成功")
        } else {
            EasyPermissions.requestPermissions(this, "申请网络权限", RC_NETWORK, *perms)
        }
    }

    /**
     * 2、获取是否有电话权限
     */
    private fun havePhonePerm(): Boolean {
        val perms = arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CALL_LOG,  //Manifest.permission.ADD_VOICEMAIL,
                Manifest.permission.WRITE_CALL_LOG,
                Manifest.permission.USE_SIP,
                Manifest.permission.PROCESS_OUTGOING_CALLS
        )
        return EasyPermissions.hasPermissions(this, *perms)
    }

    /**
     * 申请电话权限
     */
    @AfterPermissionGranted(RC_PHONE)
    fun requestPhonePerm() {
        val perms = arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CALL_LOG,  //Manifest.permission.ADD_VOICEMAIL,
                Manifest.permission.WRITE_CALL_LOG,
                Manifest.permission.USE_SIP,
                Manifest.permission.PROCESS_OUTGOING_CALLS
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            ToastUtils.showShort("电话权限已经申请成功")
        } else {
            EasyPermissions.requestPermissions(this, "申请电话权限", RC_PHONE, *perms)
        }
    }

    /**
     * 3、获取是否有日历权限
     */
    private fun haveCalendarPerm(): Boolean {
        val perms = arrayOf(
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR)
        return EasyPermissions.hasPermissions(this, *perms)
    }

    /**
     * 申请日历权限
     */
    @AfterPermissionGranted(RC_CALENDAR)
    fun requestCalendarPerm() {
        val perms = arrayOf(
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            ToastUtils.showShort("日历权限已经申请成功")
        } else {
            EasyPermissions.requestPermissions(this, "申请日历权限", RC_CALENDAR, *perms)
        }
    }

    /**
     * 4、获取是否有联系人权限
     */
    private fun haveContractsPerm(): Boolean {
        val perms = arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.GET_ACCOUNTS)
        return EasyPermissions.hasPermissions(this, *perms)
    }

    /**
     * 申请联系人权限
     */
    @AfterPermissionGranted(RC_CONTRACTS)
    fun requestContractsPerm() {
        val perms = arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.GET_ACCOUNTS)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            ToastUtils.showShort("联系人权限已经申请成功")
        } else {
            EasyPermissions.requestPermissions(this, "申请联系人权限", RC_CONTRACTS, *perms)
        }
    }

    /**
     * 5、获取是否有位置权限
     */
    private fun haveLocationPerm(): Boolean {
        val perms = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        return EasyPermissions.hasPermissions(this, *perms)
    }

    /**
     * 申请位置权限
     */
    @AfterPermissionGranted(RC_LOCATION)
    fun requestLocationPerm() {
        val perms = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            ToastUtils.showShort("位置权限已经申请成功")
        } else {
            EasyPermissions.requestPermissions(this, "申请位置权限", RC_LOCATION, *perms)
        }
    }

    /**
     * 6、获取是否有传感器权限
     */
    private fun haveSensorsPerm(): Boolean {
        val perms = arrayOf(
                Manifest.permission.BODY_SENSORS)
        return EasyPermissions.hasPermissions(this, *perms)
    }

    /**
     * 申请传感器权限
     */
    @AfterPermissionGranted(RC_SENSORS)
    fun requestSensorsPerm() {
        val perms = arrayOf(
                Manifest.permission.BODY_SENSORS)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            ToastUtils.showShort("传感器权限已经申请成功")
        } else {
            EasyPermissions.requestPermissions(this, "申请传感器权限", RC_SENSORS, *perms)
        }
    }

    /**
     * 7、获取是否有短信权限
     */
    private fun haveSmsPerm(): Boolean {
        val perms = arrayOf(
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_WAP_PUSH,
                Manifest.permission.RECEIVE_MMS)
        return EasyPermissions.hasPermissions(this, *perms)
    }

    /**
     * 申请传感器权限
     */
    @AfterPermissionGranted(RC_SMS)
    fun requestSmsPerm() {
        val perms = arrayOf(
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_WAP_PUSH,
                Manifest.permission.RECEIVE_MMS)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            ToastUtils.showShort("短信权限已经申请成功")
        } else {
            EasyPermissions.requestPermissions(this, "申请短信权限", RC_SMS, *perms)
        }
    }

    /**
     * 8、获取是否有相机权限
     */
    private fun haveCameraPerm(): Boolean {
        val perms = arrayOf(
                Manifest.permission.CAMERA
        )
        return EasyPermissions.hasPermissions(this, *perms)
    }

    /**
     * 申请相机权限权限
     */
    @AfterPermissionGranted(RC_CAMERA)
    fun requestCameraPerm() {
        val perms = arrayOf(
                Manifest.permission.CAMERA
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            ToastUtils.showShort("相机权限已经申请成功")
        } else {
            EasyPermissions.requestPermissions(this, "申请相机权限", RC_CAMERA, *perms)
        }
    }

    /**
     * 9、获取是否有读写入权限
     */
    fun haveReadAndWritePerm(): Boolean {
        val perms = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        return EasyPermissions.hasPermissions(this, *perms)
    }

    /**
     * 申请读写权限
     */
    @AfterPermissionGranted(RC_WRITE_READ)
    fun requestReadAndWritePerm() {
        val perms = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            ToastUtils.showShort("读写权限已经申请成功")
        } else {
            EasyPermissions.requestPermissions(this, "申请读写权限", RC_WRITE_READ, *perms)
        }
    }

    /**
     * 10、获取是否有录音权限
     */
    private fun haveRecodAudioPerm(): Boolean {
        val perms = arrayOf(
                Manifest.permission.RECORD_AUDIO
        )
        return EasyPermissions.hasPermissions(this, *perms)
    }

    /**
     * 申请录音的权限
     */
    @AfterPermissionGranted(RC_RECOD_AUDIO)
    fun requestRecodAudioPerm() {
        val perms = arrayOf(
                Manifest.permission.RECORD_AUDIO
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            ToastUtils.showShort("录音权限已经申请成功")
        } else {
            EasyPermissions.requestPermissions(this, "申请录音写权限", RC_RECOD_AUDIO, *perms)
        }
    }

    /**
     * 检查是否具有所有的权限
     */
    fun haveAll(): Boolean {
        return (haveNetWorkPerm()
                && havePhonePerm()
                && haveCalendarPerm()
                && haveCameraPerm()
                && haveContractsPerm()
                && haveLocationPerm()
                && haveSensorsPerm()
                && haveSmsPerm()
                && haveReadAndWritePerm()
                && haveRecodAudioPerm())
    }

    /**
     * 申请权限的方法
     */
    @AfterPermissionGranted(RC_ALL)
    fun requestPerm(context: Context?) {
        //添加所有的权限
        val perms = arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.ADD_VOICEMAIL,
                Manifest.permission.WRITE_CALL_LOG,
                Manifest.permission.USE_SIP,
                Manifest.permission.PROCESS_OUTGOING_CALLS,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.BODY_SENSORS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_WAP_PUSH,
                Manifest.permission.RECEIVE_MMS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(context!!, *perms)) {
            //提示申请成功
            ToastUtils.showShort("权限已申请成功")
        } else {
            //授予权限
            EasyPermissions.requestPermissions((context as Activity?)!!, "所有权限",
                    RC_ALL, *perms)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        // Some permissions have been granted
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        // Some permissions have been denied
        // 如果权限有没有申请成功的权限存在，则弹出弹出框，用户点击后去到设置界面自己打开权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this)
                    .build()
                    .show()
        }
    }

    /**
     * 权限申请回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 传递对应的参数，并且告知接收权限的处理者是我自己
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {
        //权限回调
        private const val RC_WRITE_READ = 0x0100
        private const val RC_NETWORK = 0x0101
        private const val RC_PHONE = 0x0102
        private const val RC_CALENDAR = 0x0103
        private const val RC_CONTRACTS = 0x0104
        private const val RC_LOCATION = 0x0105
        private const val RC_SENSORS = 0x0106
        private const val RC_SMS = 0x0107
        private const val RC_CAMERA = 0x0108
        private const val RC_RECOD_AUDIO = 0x0109
        private const val RC_ALL = 0x1111
    }
}