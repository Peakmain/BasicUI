package com.peakmain.ui.utils.launcher.stat;

import com.peakmain.ui.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：
 */
public class TaskStat {
    private static volatile String sCurrentSituation = "";
    private static List<TaskStatBean> sBeans = new ArrayList<>();
    //完成的任务数量
    private static AtomicInteger sTaskDoneCount = new AtomicInteger();
    // 是否开启统计
    private static boolean sOpenLaunchStat = false;

    public static String getCurrentSituation() {
        return sCurrentSituation;
    }
    public static void markTaskDone() {
        sTaskDoneCount.getAndIncrement();
    }
    public static void setCurrentSituation(String currentSituation) {
        if(!sOpenLaunchStat){
            return;
        }
        LogUtils.normal("currentSituation   " + currentSituation);
        sCurrentSituation = currentSituation;
        setLaunchStat();
    }
    public static void setLaunchStat() {
        TaskStatBean bean = new TaskStatBean();
        bean.setSituation(sCurrentSituation);
        bean.setCount(sTaskDoneCount.get());
        sBeans.add(bean);
        sTaskDoneCount = new AtomicInteger(0);
    }
}
