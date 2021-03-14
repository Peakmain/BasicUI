package com.peakmain.ui.utils.log;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2021/3/14
 * mail:2726449200@qq.com
 * describe：
 */
public class PLog {
    private static final String LOG_PACKAGE;

    static {
        String className = PLog.class.getName();
        LOG_PACKAGE = className.substring(0, className.lastIndexOf('.') + 1);
    }

    @IntDef({V, D, I, W, E, A})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {
    }

    public final static int V = Log.VERBOSE;
    public final static int D = Log.DEBUG;
    public final static int I = Log.INFO;
    public final static int W = Log.WARN;
    public final static int E = Log.ERROR;
    public final static int A = Log.ASSERT;

    public static void v(Object... contents) {
        log(V, contents);
    }

    public static void vt(String tag, Object... contents) {
        log(V, tag, contents);
    }

    public static void d(Object... contents) {
        log(D, contents);
    }

    public static void dt(String tag, Object... contents) {
        log(D, tag, contents);
    }

    public static void i(Object... contents) {
        log(I, contents);
    }

    public static void it(String tag, Object... contents) {
        log(I, tag, contents);
    }

    public static void w(Object... contents) {
        log(W, contents);
    }

    public static void wt(String tag, Object... contents) {
        log(W, tag, contents);
    }

    public static void e(Object... contents) {
        log(E, contents);
    }

    public static void et(String tag, Object... contents) {
        log(E, tag, contents);
    }

    public static void a(Object... contents) {
        log(A, contents);
    }

    public static void at(String tag, Object... contents) {
        log(A, tag, contents);
    }

    public static void log(@TYPE int type, Object... contents) {
        log(type, LogManager.getInstance().getConfig().getGlobalTag(), contents);
    }

    public static void log(@TYPE int type, @NonNull String tag, Object... contents) {
        log(LogManager.getInstance().getConfig(), type, tag, contents);
    }

    public static void log(@NonNull LogConfig config, @TYPE int type, String tag, Object... contents) {
        if (!config.enable()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (config.printThread()) {
            String threadInfo = LogConfig.THREAD_FORMATTER.format(Thread.currentThread());
            sb.append(threadInfo).append("\n");
        }
        if (config.stackTraceDepth() > 0) {
            String stackTrace = LogConfig.STACK_TRACE_FORMATTER.format(
                    StackTraceUtil.getCroppedRealStackTrack(
                            new Throwable().getStackTrace(), LOG_PACKAGE, config.stackTraceDepth()));
            sb.append(stackTrace).append("\n");
        }
        String body = parseBody(contents, config);
        if (body != null) {//替换转义字符\
            body = body.replace("\\\"", "\"");
        }
        sb.append(body);
        List<LogPrinter> printers =
                LogManager.getInstance().getPrinters();
        if (LogManager.getInstance().showConsolePrint()) {
            printers.add(new ConsolePrinter());
        }
        if (printers.size() == 0 &&LogManager.getInstance().showConsolePrint()) {
            LogManager.init(config, new ConsolePrinter());
        }

        //打印log
        for (LogPrinter printer : printers) {
            printer.print(config, type, tag, sb.toString());
        }
    }

    private static String parseBody(@NonNull Object[] contents, @NonNull LogConfig config) {
        if (config.injectJsonParser() != null) {
            if (contents.length == 1 && contents[0] instanceof String) {
                return (String) contents[0];
            }
            return config.injectJsonParser().toJson(contents);
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : contents) {
            sb.append(o.toString()).append(";");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }


}
