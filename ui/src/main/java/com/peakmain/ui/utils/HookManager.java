package com.peakmain.ui.utils;

import android.support.annotation.Keep;

/**
 * author:Peakmain
 * createTime:2021/7/7
 * mail:2726449200@qq.com
 * describe：
 */
public class HookManager {
    @Keep
    public static String getStack() {
        return stackTraceToString(Thread.currentThread().getStackTrace());
    }
    private static String stackTraceToString(final StackTraceElement[] arr) {
        if (arr == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (StackTraceElement stackTraceElement : arr) {
            String className = stackTraceElement.getClassName();
            // remove unused stacks
            if (className.contains("java.lang.Thread")) {
                continue;
            }

            sb.append(stackTraceElement).append(';');
        }
        return sb.toString();
    }
}
