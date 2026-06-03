package com.peakmain.ui.toast

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.peakmain.ui.BuildConfig
import com.peakmain.ui.R
import com.peakmain.ui.utils.ActivityUtils
import com.peakmain.ui.utils.SizeUtils
import com.peakmain.ui.utils.SizeUtils.getStatusBarHeight
import com.peakmain.ui.widget.ShapeLinearLayout

object TopToastUtils {

    @JvmStatic
    fun showActionToastWithFinish(message: String) {
        showActionToastWithFinish(message, 3000)
    }

    @JvmStatic
    fun showActionToastWithFinish(message: String, duration: Int) {
        val activity = ActivityUtils.mInstance.getTopActivity(true) ?: return
        showToastBar(activity, message, duration, R.color.ui_color_666666)
        activity.window.decorView.postDelayed(activity::finish, duration.toLong())
    }

    // ==================== Error Toast ====================

    @JvmStatic
    fun showErrorToast(message: String) {
        showErrorToast(message, 3000)
    }

    @JvmStatic
    fun showErrorToast(message: String, duration: Int) {
        showErrorToast(ActivityUtils.mInstance.getTopActivity(true), message, duration)
    }

    @JvmStatic
    fun showErrorToast(activity: Activity?, message: String) {
        showErrorToast(activity, message, 3000)
    }

    @JvmStatic
    fun showErrorToast(activity: Activity?, message: String, duration: Int) {
        if (activity == null) return
        if (!BuildConfig.DEBUG && isExceptionMessage(message)) return
        showToastBar(activity, message, duration, R.color.ui_color_A5534D)
    }

    // ==================== Success Toast ====================

    @JvmStatic
    fun showSuccessToast(message: String) {
        showSuccessToast(message, 3000)
    }

    @JvmStatic
    fun showSuccessToast(message: String, duration: Int) {
        showSuccessToast(ActivityUtils.mInstance.getTopActivity(true), message, duration)
    }

    @JvmStatic
    fun showSuccessToast(activity: Activity?, message: String) {
        showSuccessToast(activity, message, 3000)
    }

    @JvmStatic
    fun showSuccessToast(activity: Activity?, message: String, duration: Int) {
        if (activity == null) return
        showToastBar(activity, message, duration, R.color.ui_color_1F401B)
    }

    // ==================== Action Toast ====================

    @JvmStatic
    fun showActionToast(message: String) {
        showActionToast(message, 3000)
    }

    @JvmStatic
    fun showActionToast(message: String, duration: Int) {
        showActionToast(ActivityUtils.mInstance.getTopActivity(true), message, duration)
    }

    @JvmStatic
    fun showActionToast(activity: Activity?, message: String) {
        showActionToast(activity, message, 3000)
    }

    @JvmStatic
    fun showActionToast(activity: Activity?, message: String, duration: Int) {
        if (activity == null) return
        showToastBar(activity, message, duration, R.color.ui_color_666666)
    }

    // ==================== Dialog Toast ====================

    private var sCurrentDialog: Dialog? = null

    @JvmStatic
    fun showErrorDialogToast(message: String) {
        showErrorDialogToast(message, 3000)
    }

    @JvmStatic
    fun showErrorDialogToast(message: String, duration: Int) {
        showErrorDialogToast(ActivityUtils.mInstance.getTopActivity(true), message, duration)
    }

    @JvmStatic
    fun showErrorDialogToast(activity: Activity?, message: String) {
        showErrorDialogToast(activity, message, 3000)
    }

    @JvmStatic
    fun showErrorDialogToast(activity: Activity?, message: String, duration: Int) {
        if (activity == null) return
        if (!BuildConfig.DEBUG && isExceptionMessage(message)) return
        showDialogToast(activity, message, duration, R.color.ui_color_A5534D)
    }

    @JvmStatic
    fun showSuccessDialogToast(message: String) {
        showSuccessDialogToast(message, 3000)
    }

    @JvmStatic
    fun showSuccessDialogToast(message: String, duration: Int) {
        showSuccessDialogToast(ActivityUtils.mInstance.getTopActivity(true), message, duration)
    }

    @JvmStatic
    fun showSuccessDialogToast(activity: Activity?, message: String) {
        showSuccessDialogToast(activity, message, 3000)
    }

    @JvmStatic
    fun showSuccessDialogToast(activity: Activity?, message: String, duration: Int) {
        if (activity == null) return
        showDialogToast(activity, message, duration, R.color.ui_color_1F401B)
    }

    @JvmStatic
    fun showActionDialogToast(message: String) {
        showActionDialogToast(message, 3000)
    }

    @JvmStatic
    fun showActionDialogToast(message: String, duration: Int) {
        showActionDialogToast(ActivityUtils.mInstance.getTopActivity(true), message, duration)
    }

    @JvmStatic
    fun showActionDialogToast(activity: Activity?, message: String) {
        showActionDialogToast(activity, message, 3000)
    }

    @JvmStatic
    fun showActionDialogToast(activity: Activity?, message: String, duration: Int) {
        if (activity == null) return
        if (!BuildConfig.DEBUG && isExceptionMessage(message)) return
        showDialogToast(activity, message, duration, R.color.ui_color_333333)
    }

    // ==================== Internal ====================

    private fun isExceptionMessage(message: String): Boolean {
        return message.matches(Regex(".*\\b[a-zA-Z0-9_.]*((Exception)|(Error)|(Throwable))\\b.*"))
    }

    private fun showToastBar(
        activity: Activity,
        message: String,
        duration: Int,
        backgroundColor: Int
    ) {
        ToastBar.build(activity)
            .setCustomView(R.layout.layout_toast_tips)
            .setBackground(backgroundColor)
            .setDuration(duration.toLong())
            .setCustomViewInitializer { view ->
                view.findViewById<TextView>(R.id.tv_message).text = message
            }
            .show()
    }

    private fun showDialogToast(
        activity: Activity,
        message: String,
        duration: Int,
        backgroundColor: Int
    ) {
        sCurrentDialog?.let { if (it.isShowing) it.dismiss() }

        val dialog = Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.setContentView(R.layout.layout_toast_tips)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        dialog.findViewById<ShapeLinearLayout>(R.id.toast_container)?.let { containerView ->
            containerView.setNormalBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    backgroundColor
                )
            )
            containerView.setPadding(
                containerView.paddingLeft,
                containerView.paddingTop + getStatusBarHeight(),
                containerView.paddingRight,
                containerView.paddingBottom
            )
        }
        dialog.findViewById<TextView>(R.id.tv_message)?.text = message

        dialog.window?.apply {
            attributes = attributes.apply {
                gravity = Gravity.TOP
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
            setWindowAnimations(R.style.TopDialogAnimation)
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        sCurrentDialog = dialog
        dialog.setOnDismissListener { sCurrentDialog = null }
        dialog.show()

        dialog.findViewById<TextView>(R.id.tv_message)
            ?.postDelayed(dialog::dismiss, duration.toLong())
    }
}