package com.peakmain.ui.utils.file;

import android.support.annotation.NonNull;

import com.peakmain.ui.constants.PermissionConstants;
import com.peakmain.ui.utils.PermissionUtils;
import com.peakmain.ui.utils.ToastUtils;
import com.peakmain.ui.utils.database.DaoSupport;

import java.io.File;
import java.util.List;

/**
 * author:Peakmain
 * createTime:2021/7/7
 * mail:2726449200@qq.com
 * describe：
 */
public class FileLogger implements Comparable<FileLogger> {
    /**
     * 这对应一个 native 层的地址
     */
    private long mNativePtr;

    /**
     * 当前所对应的 File log本地文件
     */
    File mLogFile;

    FileLogger(File logFile, int maxFileSize) {
        this.mLogFile = logFile;
        mNativePtr = nativeLogFileCreate(logFile.getAbsolutePath(), maxFileSize);
    }

    private native long nativeLogFileCreate(String logPath, int maxFileSize);

    public void writeLog(String content) {
        byte[] data = content.getBytes();
        nativeWriteData(mNativePtr, data, data.length);
    }

    private native void nativeWriteData(long nativePtr, byte[] data, int dataLen);

    public void release() {
        nativeRelease(mNativePtr);
    }

    private native void nativeRelease(long nativePtr);

    @Override
    public int compareTo( FileLogger other) {
        return mLogFile.getName().compareToIgnoreCase(other.mLogFile.getName());
    }

    /**
     * 返回文件内容的真实大小
     *
     * @return 文件大小
     */
    public int length() {
        return nativeLogFileLength(mNativePtr);
    }

    private native int nativeLogFileLength(long nativePtr);

}
