package com.peakmain.ui.utils.log;

/**
 * author ：Peakmain
 * createTime：2021/3/14
 * mail:2726449200@qq.com
 * describe：日志进行格式化
 */
public interface LogFormatter<T> {
    String format(T message);
}
