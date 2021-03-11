package com.peakmain.ui.utils.launcher.task

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：主任务
 */
abstract class MainTask : Task() {
    override fun runOnMainThread(): Boolean {
        return true
    }
}