package com.peakmain.ui.utils.log;

/**
 * author ：Peakmain
 * createTime：2021/3/14
 * mail:2726449200@qq.com
 * describe：
 */
public class ThreadFormatter implements LogFormatter<Thread> {
    @Override
    public String format(Thread thread) {
        return "current Thread:"+thread.getName();
    }
}
