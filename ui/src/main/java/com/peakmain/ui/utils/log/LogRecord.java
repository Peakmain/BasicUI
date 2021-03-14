package com.peakmain.ui.utils.log;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * author ：Peakmain
 * createTime：2021/3/14
 * mail:2726449200@qq.com
 * describe：
 */
public class LogRecord {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.CHINA);
    public long timeMillis;
    public int level;
    public String tag;
    public String message;

    public LogRecord(long timeMillis, int level, String tag, String message) {
        this.timeMillis = timeMillis;
        this.level = level;
        this.tag = tag;
        this.message = message;
    }

    public String flattenedLog() {
        return getFlattened() + "\n" + message;
    }

    public String getFlattened() {
        return format(timeMillis) + '|' + level + '|' + tag + "|:";
    }

    private String format(long timeMillis) {
        return sdf.format(timeMillis);
    }
}
