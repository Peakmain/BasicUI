package com.peakmain.ui.utils.log;

/**
 * author ：Peakmain
 * createTime：2021/3/14
 * mail:2726449200@qq.com
 * describe：堆栈信息进行格式化
 */
public class StackTraceFormatter implements LogFormatter<StackTraceElement[]> {
    @Override
    public String format(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder(128);
        if (stackTrace == null || stackTrace.length == 0) {
            return null;
        } else if (stackTrace.length == 1) {
            return "\t─ " + stackTrace[0].toString();
        } else {
            for (int i = 0, len = stackTrace.length; i < len; i++) {
                if (i == 0) {
                    sb.append("stackTrace:  \n");
                }
                if (i != len - 1) {
                    sb.append("\t├ ");
                    sb.append(stackTrace[i].toString());
                    sb.append("\n");
                } else {
                    sb.append("\t└ ");
                    sb.append(stackTrace[i].toString());
                }
            }
            return sb.toString();
        }
    }
}
