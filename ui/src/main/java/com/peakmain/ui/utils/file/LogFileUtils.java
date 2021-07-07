package com.peakmain.ui.utils.file;

import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author ：Peakmain
 * createTime：2021/7/6
 * describe：基于mmap的日志打印
 */
public class LogFileUtils {
    static {
        System.loadLibrary("compress-lib");
    }

    private static int mFileTypeSize = 5;
    private static int mMaxFileSize = 5 * 1024 * 1024;
    private static String mPath;
    private static Map<String, List<FileLogger>> mFileLoggerMaps = new HashMap<>();

    public static FileLogger getLogger(String name) {
        if (TextUtils.isEmpty(mPath)) {
            throw new NullPointerException("Log the parent directory is empty, please call the init method Settings.");
        }
        List<FileLogger> fileLoggers = mFileLoggerMaps.get(name);
        if (fileLoggers == null) {
            fileLoggers = new ArrayList<>();
            mFileLoggerMaps.put(name, fileLoggers);
        }
        return getFileLogger(fileLoggers, name);
    }

    private static FileLogger getFileLogger(List<FileLogger> fileLoggers, String modeName) {
        FileLogger fileLogger;
        if (isEmpty(fileLoggers)) {
            fileLogger = new FileLogger(new File(getLogFileName(modeName)), mMaxFileSize);
            fileLoggers.add(fileLogger);
        } else {
            int typeSize = fileLoggers.size();
            fileLogger = fileLoggers.get(typeSize - 1);
            if (fileLogger.length() >= mMaxFileSize) {
                fileLogger.release();
                if (typeSize == mFileTypeSize) {
                    FileLogger deletLogger = fileLoggers.remove(0);
                    deletLogger.mLogFile.delete();
                    typeSize--;
                }
                fileLogger=new FileLogger(new File(getLogFileName(modeName)),mMaxFileSize);
                fileLoggers.add(fileLogger);
            }
        }
        return fileLogger;
    }

    public static void init(int fileTypeSize, int maxFileSize, String path) throws IOException {
        mFileTypeSize = fileTypeSize;
        mMaxFileSize = maxFileSize;
        mPath = path;
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("path is not exists.");
        }
        if (!file.isDirectory()) {
            throw new IOException("path is not directory");
        }
        File[] logFiles = file.listFiles();
        if (logFiles == null) {
            return;
        }
        for (File logFile : logFiles) {
            if (logFile.isFile()) {
                String fileName = logFile.getName();
                String name = fileName.substring(0, fileName.indexOf("-"));
                List<FileLogger> fileLoggers = mFileLoggerMaps.get(name);
                if (fileLoggers == null) {
                    fileLoggers = new ArrayList<>();
                    mFileLoggerMaps.put(name, fileLoggers);
                }
                FileLogger fileLogger = new FileLogger(logFile, mMaxFileSize);
                fileLoggers.add(fileLogger);
            }
        }
        Set<String> keys = mFileLoggerMaps.keySet();
        for (String key : keys) {
            Collections.sort(mFileLoggerMaps.get(key));
        }
    }

    /**
     * 格式化当前日期时间
     */
    public static String getLogFileName(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        return mPath + "/" + name + "-" + sdf.format(System.currentTimeMillis()) + ".log";
    }

    private static final boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
