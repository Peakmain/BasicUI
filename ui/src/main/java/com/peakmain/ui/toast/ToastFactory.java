package com.peakmain.ui.toast;

import android.content.Context;

import androidx.core.app.NotificationManagerCompat;

/**
 * author ：Peakmain
 * createTime：2023/11/10
 * mail:2726449200@qq.com
 * describe：
 */

public class ToastFactory {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    private final int mCheckNotification = -1;
    private volatile static ToastFactory sToastFactory;

    private final IToast mIToast;

    private ToastFactory(Context context) {
        if (isNotificationEnabled(context)) {
            mIToast = new SystemToast(context);
        } else {
            mIToast = new CustomToast(context);
        }
    }

    public static IToast getInstance(Context context) {
        if (sToastFactory == null) {
            synchronized (ToastFactory.class) {
                if (sToastFactory == null) {
                    sToastFactory = new ToastFactory(context);
                }
            }
        }
        return sToastFactory.mIToast;
    }

    private static boolean isNotificationEnabled(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }
}