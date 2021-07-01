package com.peakmain.breakpad;

import java.io.File;

/**
 * author:Peakmain
 * createTime:2021/5/7
 * mail:2726449200@qq.com
 * describe：
 */
public class NativeCrashHandler {
    static {
        System.loadLibrary("breakpad");
    }

    //nativecrash 日志文件存放的目录
    public static void init(String crashDir) {
        initBreakPad(crashDir);
    }


    private static native void initBreakPad(String crashDir);

}
