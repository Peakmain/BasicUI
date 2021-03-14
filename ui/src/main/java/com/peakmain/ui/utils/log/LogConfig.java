package com.peakmain.ui.utils.log;

/**
 * author ：Peakmain
 * createTime：2021/3/14
 * mail:2726449200@qq.com
 * describe：
 */
public class LogConfig {
    public static int MAX_LEN = 512;
    static ThreadFormatter THREAD_FORMATTER = new ThreadFormatter();
    static StackTraceFormatter STACK_TRACE_FORMATTER = new StackTraceFormatter();

    public String getGlobalTag() {
        return "BasicUI";
    }

    //自定义注入
    public JsonParser injectJsonParser() {
        return null;
    }

    public boolean enable() {
        return true;
    }

    //是否打印线程
    public boolean printThread() {
        return false;
    }

    //打印堆栈的深度
    public int stackTraceDepth() {
        return 3;
    }

    public static class DefalutConfig extends LogConfig {
        @Override
        public boolean enable() {
            return true;
        }
    }

    public interface JsonParser {
        String toJson(Object src);
    }
}
