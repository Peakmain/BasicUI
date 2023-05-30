package com.peakmain.ui.utils.log;


import static com.peakmain.ui.utils.log.LogConfig.MAX_LEN;

import android.util.Log;

import androidx.annotation.NonNull;


/**
 * author ：Peakmain
 * createTime：2021/3/14
 * mail:2726449200@qq.com
 * describe：控制台打印
 */
public class ConsolePrinter implements LogPrinter {
    @Override
    public void print(@NonNull LogConfig config, int level, String tag, @NonNull String printString) {
        //打印的长度
        int length = printString.length();
        int countOfStub = length / MAX_LEN;
        if (countOfStub > 0) {
            StringBuilder sb = new StringBuilder();
            int index = 0;
            for (int i = 0; i < countOfStub; i++) {
                sb.append(printString, index, index + MAX_LEN);
                index = index + MAX_LEN;
            }
            if (index != length) {
                sb.append(printString, index, length);
            }
            Log.println(level, tag, sb.toString());
        } else {
            //一行
            Log.println(level, tag, printString);
        }
    }
}
