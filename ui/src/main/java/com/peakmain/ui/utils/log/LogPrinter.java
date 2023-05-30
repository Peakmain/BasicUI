package com.peakmain.ui.utils.log;


import androidx.annotation.NonNull;

/**
 * author ：Peakmain
 * createTime：2021/3/14
 * mail:2726449200@qq.com
 * describe：打印接口
 */
public interface LogPrinter {
    void print(@NonNull LogConfig config, int level, String tag, @NonNull String printString);
}
