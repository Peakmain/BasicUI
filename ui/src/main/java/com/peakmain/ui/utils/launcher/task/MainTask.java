package com.peakmain.ui.utils.launcher.task;

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：主任务
 */
public abstract class MainTask extends Task{
    @Override
    public boolean runOnMainThread() {
        return true;
    }
}
