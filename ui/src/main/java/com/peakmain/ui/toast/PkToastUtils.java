package com.peakmain.ui.toast;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.peakmain.ui.BuildConfig;
import com.peakmain.ui.R;
import com.peakmain.ui.utils.ActivityUtils;

/**
 * @author ：Peakmain
 * createTime：2026/6/2
 * mail:2726449200@qq.com
 * describe：
 */
public class PkToastUtils {
    private static Dialog sCurrentDialog;
    public static void showActionToastWithFinish(String message) {
        showActionToast(ActivityUtils.getMInstance().getTopActivity(true), message);
    }

    public static void showActionToastWithFinish(String message, int duration) {
        Activity activity = ActivityUtils.getMInstance().getTopActivity(true);
        if (activity == null) return;
        CookieBar.build(activity)
                .setCustomView(R.layout.layout_toast_tips)
                .setCookiePosition(CookieBar.TOP)
                .setBackground(R.drawable.shape_toast_grey)
                .setDuration(duration)
                .setCustomViewInitializer(view -> ((TextView) view.findViewById(R.id.tv_message)).setText(message))
                .show();
        // 延迟关闭页面
        activity.getWindow().getDecorView().postDelayed(activity::finish, duration);
    }

    public static void showErrorToast(String message) {
        showErrorToast(ActivityUtils.getMInstance().getTopActivity(true), message);
    }

    public static void showErrorToast(String message, int duration) {
        showErrorToast(ActivityUtils.getMInstance().getTopActivity(true), message, duration);
    }

    /**
     * 成功提示（自动获取顶部Activity）
     */
    public static void showSuccessToast(String message) {
        showSuccessTipsToast(ActivityUtils.getMInstance().getTopActivity(true), message);
    }

    public static void showSuccessToast(String message, int duration) {
        Activity activity = ActivityUtils.getMInstance().getTopActivity(true);
        if (activity == null) return;
        CookieBar.build(activity)
                .setCustomView(R.layout.layout_toast_tips)
                .setCookiePosition(CookieBar.TOP)
                .setBackground(R.drawable.shape_toast_tips)
                .setDuration(duration)
                .setCustomViewInitializer(view -> ((TextView) view.findViewById(R.id.tv_message)).setText(message))
                .show();
    }

    /**
     * 常规提示（自动获取顶部Activity）
     */
    public static void showActionToast(String message) {
        showActionToast(ActivityUtils.getMInstance().getTopActivity(true), message);
    }

    public static void showActionToast(String message, int duration) {
        Activity activity = ActivityUtils.getMInstance().getTopActivity(true);
        if (activity == null) return;
        CookieBar.build(activity)
                .setCustomView(R.layout.layout_toast_tips)
                .setCookiePosition(CookieBar.TOP)
                .setBackground(R.drawable.shape_toast_grey)
                .setDuration(duration)
                .setCustomViewInitializer(view -> ((TextView) view.findViewById(R.id.tv_message)).setText(message))
                .show();
    }

    // ==================== Dialog Toast 便捷方法（自动获取顶部Activity） ====================
    public static void showErrorDialogToast(String message) {
        showErrorDialogToast(ActivityUtils.getMInstance().getTopActivity(true), message);
    }

    public static void showErrorDialogToast(String message, int duration) {
        showErrorDialogToast(ActivityUtils.getMInstance().getTopActivity(true), message, duration);
    }

    public static void showSuccessDialogToast(String message) {
        showSuccessDialogToast(ActivityUtils.getMInstance().getTopActivity(true), message);
    }

    public static void showSuccessDialogToast(String message, int duration) {
        showSuccessDialogToast(ActivityUtils.getMInstance().getTopActivity(true), message, duration);
    }

    public static void showActionDialogToast(String message) {
        showActionDialogToast(ActivityUtils.getMInstance().getTopActivity(true), message);
    }

    public static void showActionDialogToast(String message, int duration) {
        showActionDialogToast(ActivityUtils.getMInstance().getTopActivity(true), message, duration);
    }
    /**
     * 常规提示
     */
    public static void showActionToast(Activity activity, String message) {
        if (activity == null) return;
        CookieBar.build(activity)
                .setCustomView(R.layout.layout_toast_tips)
                .setCookiePosition(CookieBar.TOP)
                .setBackground(R.drawable.shape_toast_grey)
                .setDuration(3000)
                .setCustomViewInitializer(view -> ((TextView) view.findViewById(R.id.tv_message)).setText(message))
                .show();
    }


    public static void showErrorToast(Activity activity, String message) {
        showErrorToast(activity, message, 3000);
    }



    private static boolean isExceptionMessage(String message) {
        // 匹配以类名形式出现的异常，例如 java.lang.XXXException
        return message.matches(".*\\b[a-zA-Z0-9_.]*((Exception)|(Error)|(Throwable))\\b.*");
    }

    private static void showErrorToast(Activity activity, String message, int duration) {
        if (activity == null) return;
        // 如果 message 包含异常类名，直接拦截
        if (!BuildConfig.DEBUG && isExceptionMessage(message)) {
            return;
        }
        CookieBar.build(activity)
                .setCustomView(R.layout.layout_toast_tips)
                .setCookiePosition(CookieBar.TOP)
                .setBackground(R.drawable.shape_toast_error)
                .setDuration(duration)
                .setCustomViewInitializer(view -> {
                            TextView text = view.findViewById(R.id.tv_message);
                            text.setText(message);
                        }
                )
                .show();
    }



    public static void showErrorDialogToast(Activity activity, String message) {
        showErrorDialogToast(activity, message, 3000);
    }

    public static void showErrorDialogToast(Activity activity, String message, int duration) {
        if (activity == null) return;
        if (!BuildConfig.DEBUG && isExceptionMessage(message)) {
            return;
        }
        showDialogToastInternal(activity, message, duration, R.drawable.shape_toast_error);
    }

    public static void showSuccessDialogToast(Activity activity, String message) {
        showSuccessDialogToast(activity, message, 3000);
    }



    public static void showSuccessDialogToast(Activity activity, String message, int duration) {
        if (activity == null) return;
        showDialogToastInternal(activity, message, duration, R.drawable.shape_toast_tips);
    }



    public static void showActionDialogToast(Activity activity, String message) {
        showActionDialogToast(activity, message, 3000);
    }


    public static void showActionDialogToast(Activity activity, String message, int duration) {
        if (activity == null) return;
        if (!BuildConfig.DEBUG && isExceptionMessage(message)) {
            return;
        }
        showDialogToastInternal(activity, message, duration, R.drawable.shape_toast_grey);
    }

    /**
     * 内部通用Dialog Toast实现
     */
    private static void showDialogToastInternal(Activity activity, String message, int duration, int backgroundRes) {
        // 先取消上一个正在显示的Dialog
        if (sCurrentDialog != null && sCurrentDialog.isShowing()) {
            sCurrentDialog.dismiss();
            sCurrentDialog = null;
        }

        // ==================== 顶部 Dialog 实现（复用你的 toast_tips 布局） ====================
        Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.layout_toast_tips); // 直接复用你现有的布局
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.findViewById(R.id.cookie).setBackground(ContextCompat.getDrawable(dialog.getContext(), backgroundRes));

        // 设置文本
        TextView tvMessage = dialog.findViewById(R.id.tv_message);
        tvMessage.setText(message);

        // ==================== 核心：Dialog 显示在顶部 ====================
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();

            params.gravity = Gravity.TOP;       // 顶部
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;

            // ==================== 关键：顶部进出动画 ====================
            window.setWindowAnimations(R.style.TopDialogAnimation);
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
            window.setAttributes(params);
        }

        // 保存当前Dialog引用
        sCurrentDialog = dialog;

        // 设置关闭监听，清空引用避免内存泄漏
        dialog.setOnDismissListener(d -> {
            if (sCurrentDialog == d) {
                sCurrentDialog = null;
            }
        });

        dialog.show();

        // ==================== 自动消失（和 Toast 时长保持一致） ====================
        tvMessage.postDelayed(dialog::dismiss, duration);
    }


    /**
     * 成功提示
     */
    public static void showSuccessTipsToast(Activity activity, String message) {
        if (activity == null) return;
        CookieBar.build(activity)
                .setCustomView(R.layout.layout_toast_tips)
                .setCookiePosition(CookieBar.TOP)
                .setBackground(R.drawable.shape_toast_tips)
                .setDuration(3000)
                .setCustomViewInitializer(view -> {
                            TextView text = view.findViewById(R.id.tv_message);
                            text.setText(message);
                        }
                )
                .show();
    }

}
